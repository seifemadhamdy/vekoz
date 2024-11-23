package seifemadhamdy.vekoz.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
      _queriedMoviesUiState.asStateFlow()

  private val _isSearchBarVisible = MutableStateFlow<Boolean>(false)
  val isSearchBarVisible: StateFlow<Boolean> = _isSearchBarVisible.asStateFlow()

  private val _movieQuery = MutableStateFlow<String?>(null)
  val movieQuery: StateFlow<String?> = _movieQuery.asStateFlow()

  private val _watchlistStates = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
  val watchlistStates = _watchlistStates.asStateFlow()

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

  private fun fetchMoviesByQuery(query: String) {
    viewModelScope.launch {
      _queriedMoviesUiState.update { UiState.Loading }

      tmdbSearchRepository.getMoviesByQuery(query = query).collect { networkResult ->
        _queriedMoviesUiState.value =
            when (networkResult) {
              is NetworkResult.Error -> UiState.Error(networkResult.message ?: "Unknown Error")
              is NetworkResult.Success -> UiState.Success(data = networkResult.data.results)
            }
      }
    }
  }

  fun addMovieToWatchlist(movieId: Int) {
    viewModelScope.launch {
      watchlistRepository.addToWatchlist(movieId = movieId)
      _watchlistStates.update { it + (movieId to true) }
    }
  }

  fun removeMovieFromWatchlist(movieId: Int) {
    viewModelScope.launch {
      watchlistRepository.removeFromWatchlist(movieId = movieId)
      _watchlistStates.update { it + (movieId to false) }
    }
  }

  fun isMovieInWatchlist(movieId: Int): StateFlow<Boolean> =
      watchlistStates
          .map { it[movieId] == true }
          .stateIn(viewModelScope, SharingStarted.Lazily, false)

  fun initializeWatchlistState(movieId: Int) {
    viewModelScope.launch(Dispatchers.IO) {
      val isInWatchlist = watchlistRepository.isInWatchlist(movieId = movieId)
      _watchlistStates.update { it + (movieId to isInWatchlist) }
    }
  }

  fun toggleSearchBarVisibility() {
    resetMovieQuery()
    _isSearchBarVisible.update { !it }
  }

  fun updateMovieQuery(query: String) {
    _movieQuery.update { query }
    fetchMoviesByQuery(query = query)
  }

  private fun resetMovieQuery() {
    if (movieQuery.value != null) _movieQuery.update { null }
  }
}
