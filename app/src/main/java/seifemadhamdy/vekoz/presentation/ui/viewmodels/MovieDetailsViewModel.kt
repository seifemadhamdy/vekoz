package seifemadhamdy.vekoz.presentation.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import seifemadhamdy.vekoz.data.remote.dto.MovieCastDto
import seifemadhamdy.vekoz.data.remote.dto.MovieCreditsCrewDto
import seifemadhamdy.vekoz.data.remote.dto.MovieDetailsResponseDto
import seifemadhamdy.vekoz.data.remote.dto.ResultsDto
import seifemadhamdy.vekoz.data.remote.result.NetworkResult
import seifemadhamdy.vekoz.domain.models.TmdbMoviesRepository
import seifemadhamdy.vekoz.domain.repositories.WatchlistRepository
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val tmdbMoviesRepository: TmdbMoviesRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

  companion object {
    sealed interface UiState<out T> {
      object Loading : UiState<Nothing>

      data class Success<T>(val data: T) : UiState<T>

      data class Error(val message: String) : UiState<Nothing>
    }
  }

  private val movieId: Int = savedStateHandle["movieId"]!!

  private val _movieDetailsUiState =
      MutableStateFlow<UiState<MovieDetailsResponseDto>>(UiState.Loading)
  val movieDetailsUiState: StateFlow<UiState<MovieDetailsResponseDto>> =
      _movieDetailsUiState.asStateFlow()

  private val _similarMoviesUiState = MutableStateFlow<UiState<List<ResultsDto>>>(UiState.Loading)
  val similarMoviesUiState: StateFlow<UiState<List<ResultsDto>>> =
      _similarMoviesUiState.asStateFlow()

  private val _movieCreditsUiState =
      MutableStateFlow<UiState<Pair<List<MovieCastDto>, List<MovieCreditsCrewDto>>>>(
          UiState.Loading)
  val movieCreditsUiState: StateFlow<UiState<Pair<List<MovieCastDto>, List<MovieCreditsCrewDto>>>> =
      _movieCreditsUiState.asStateFlow()

  private val _isMovieInWatchlist = MutableStateFlow<Boolean>(false)
  val isMovieInWatchlist: StateFlow<Boolean> = _isMovieInWatchlist.asStateFlow()

  init {
    fetchMovieDetails()
    viewModelScope.launch { isMovieInWatchlist().collect { _isMovieInWatchlist.update { it } } }
  }

  fun fetchMovieDetails() {
    viewModelScope.launch {
      _movieDetailsUiState.update { UiState.Loading }

      tmdbMoviesRepository.getMovieDetails(movieId = movieId).collect { networkResult ->
        _movieDetailsUiState.value =
            when (networkResult) {
              is NetworkResult.Error -> UiState.Error(networkResult.message ?: "Unknown Error")
              is NetworkResult.Success -> UiState.Success(data = networkResult.data)
            }
      }
    }
  }

  fun fetchSimilarMovies() {
    viewModelScope.launch {
      _similarMoviesUiState.update { UiState.Loading }

      tmdbMoviesRepository.getSimilarMovies(movieId = movieId).collect { networkResult ->
        _similarMoviesUiState.value =
            when (networkResult) {
              is NetworkResult.Error -> UiState.Error(networkResult.message ?: "Unknown Error")
              is NetworkResult.Success -> UiState.Success(data = networkResult.data.results)
            }
      }
    }
  }

  fun fetchSimilarMoviesAndCast() {
    viewModelScope.launch {
      _movieCreditsUiState.update { UiState.Loading }

      tmdbMoviesRepository
          .getSimilarMovies(movieId)
          .catch { exception ->
            _movieCreditsUiState.value = UiState.Error(exception.message ?: "Unknown Error")
          }
          .collect { similarMoviesResult ->
            when (similarMoviesResult) {
              is NetworkResult.Error -> {
                _movieCreditsUiState.value =
                    UiState.Error(similarMoviesResult.message ?: "Unknown Error")
              }

              is NetworkResult.Success -> {
                val actors = mutableListOf<MovieCastDto>()
                val directors = mutableListOf<MovieCreditsCrewDto>()

                similarMoviesResult.data.results.forEach { movie ->
                  movie.id?.let { movieId ->
                    tmdbMoviesRepository
                        .getMovieCredits(movieId)
                        .catch { exception ->
                          Log.e("MovieCredits", "Failed to fetch credits: ${exception.message}")
                        }
                        .collect { creditResult ->
                          when (creditResult) {
                            is NetworkResult.Success -> {
                              actors.addAll(
                                  creditResult.data.movieCastDto.filter {
                                    it.knownForDepartment == "Acting"
                                  })

                              directors.addAll(
                                  creditResult.data.movieCreditsCrewDto.filter {
                                    it.department == "Directing"
                                  })
                            }
                            is NetworkResult.Error -> {
                              Log.e(
                                  "MovieCredits", "Error getting credits: ${creditResult.message}")
                            }
                          }
                        }
                  }
                }

                val sortedActors =
                    actors.distinctBy { it.id }.sortedByDescending { it.popularity }.take(5)

                val sortedDirectors =
                    directors.distinctBy { it.id }.sortedByDescending { it.popularity }.take(5)

                if (sortedActors.isEmpty() && sortedDirectors.isEmpty()) {
                  _movieCreditsUiState.value = UiState.Error("No cast or crew data found")
                } else {
                  _movieCreditsUiState.value = UiState.Success(Pair(sortedActors, sortedDirectors))
                }
              }
            }
          }
    }
  }

  fun addMovieToWatchlist() {
    viewModelScope.launch {
      watchlistRepository.addToWatchlist(movieId = movieId)
      _isMovieInWatchlist.update { true }
    }
  }

  fun removeMovieFromWatchlist() {
    viewModelScope.launch {
      watchlistRepository.removeFromWatchlist(movieId = movieId)
      _isMovieInWatchlist.update { false }
    }
  }

  fun isMovieInWatchlist(): Flow<Boolean> =
      flow { emit(watchlistRepository.isInWatchlist(movieId = movieId)) }.flowOn(Dispatchers.IO)
}
