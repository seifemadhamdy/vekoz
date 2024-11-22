package seifemadhamdy.vekoz.presentation.ui.viewmodels

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
import seifemadhamdy.vekoz.data.remote.dto.ResultsDto
import seifemadhamdy.vekoz.data.remote.result.NetworkResult
import seifemadhamdy.vekoz.domain.models.TmdbMoviesRepository
import seifemadhamdy.vekoz.domain.models.TmdbSearchRepository
import seifemadhamdy.vekoz.domain.repositories.WatchlistRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val tmdbMoviesRepository: TmdbMoviesRepository,
    private val tmdbSearchRepository: TmdbSearchRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

  companion object {
    sealed interface UiState<out T> {
      object Loading : UiState<Nothing>

      data class Success<T>(val data: T) : UiState<T>

      data class Error(val message: String) : UiState<Nothing>
    }
  }

  private val _popularMoviesUiState = MutableStateFlow<UiState<List<ResultsDto>>>(UiState.Loading)
  val popularMovesUiState: StateFlow<UiState<List<ResultsDto>>> =
      _popularMoviesUiState.asStateFlow()

  private val _queriedMoviesUiState = MutableStateFlow<UiState<List<ResultsDto>>>(UiState.Loading)
  val queriedMoviesUiState: StateFlow<UiState<List<ResultsDto>>> =
      _popularMoviesUiState.asStateFlow()

  init {
    fetchPopularMovies()
  }

  fun fetchPopularMovies() {
    viewModelScope.launch {
      _popularMoviesUiState.update { UiState.Loading }

      tmdbMoviesRepository.getPopularMovies().collect { networkResult ->
        _popularMoviesUiState.value =
            when (networkResult) {
              is NetworkResult.Error -> UiState.Error(networkResult.message ?: "Unknown Error")
              is NetworkResult.Success -> UiState.Success(data = networkResult.data.results)
            }
      }
    }
  }

  fun fetchMoviesByQuery(query: String) {
    viewModelScope.launch {
      _queriedMoviesUiState.update { UiState.Loading }

      tmdbSearchRepository.getMoviesByQuery(query = query).collect { networkResult ->
        _popularMoviesUiState.value =
            when (networkResult) {
              is NetworkResult.Error -> UiState.Error(networkResult.message ?: "Unknown Error")
              is NetworkResult.Success -> UiState.Success(data = networkResult.data.results)
            }
      }
    }
  }

  fun addMovieToWatchlist(movieId: Int) {
    viewModelScope.launch { watchlistRepository.addToWatchlist(movieId = movieId) }
  }

  fun removeMovieFromWatchlist(movieId: Int) {
    viewModelScope.launch { watchlistRepository.removeFromWatchlist(movieId = movieId) }
  }

  fun isMovieInWatchlist(movieId: Int): Flow<Boolean> =
      flow { emit(watchlistRepository.isInWatchlist(movieId = movieId)) }.flowOn(Dispatchers.IO)
}
