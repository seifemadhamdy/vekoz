package seifemadhamdy.vekoz.presentation.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import seifemadhamdy.vekoz.data.remote.dto.MovieCreditsResponseDto
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
      MutableStateFlow<UiState<MovieCreditsResponseDto>>(UiState.Loading)
  val movieCreditsUiState: StateFlow<UiState<MovieCreditsResponseDto>> =
      _movieCreditsUiState.asStateFlow()

  init {
    fetchMovieDetails()
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

  fun fetchMovieCredits() {
    viewModelScope.launch {
      _movieCreditsUiState.update { UiState.Loading }

      tmdbMoviesRepository.getMovieCredits(movieId = movieId).collect { networkResult ->
        _movieCreditsUiState.value =
            when (networkResult) {
              is NetworkResult.Error -> UiState.Error(networkResult.message ?: "Unknown Error")
              is NetworkResult.Success -> UiState.Success(data = networkResult.data)
            }
      }
    }
  }

  fun addMovieToWatchlist() {
    viewModelScope.launch { watchlistRepository.addToWatchlist(movieId = movieId) }
  }

  fun removeMovieFromWatchlist() {
    viewModelScope.launch { watchlistRepository.removeFromWatchlist(movieId = movieId) }
  }

  fun isMovieInWatchlist(): Flow<Boolean> =
      flow { emit(watchlistRepository.isInWatchlist(movieId = movieId)) }.flowOn(Dispatchers.IO)
}
