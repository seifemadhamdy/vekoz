package seifemadhamdy.vekoz.presentation.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import seifemadhamdy.vekoz.data.remote.dto.MovieCastDto
import seifemadhamdy.vekoz.data.remote.dto.MovieCreditsCrewDto
import seifemadhamdy.vekoz.data.remote.dto.MovieDetailsResponseDto
import seifemadhamdy.vekoz.data.remote.dto.SimilarMoviesResponseDto
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
    private val watchlistRepository: WatchlistRepository,
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

    private val _similarMoviesUiState =
        MutableStateFlow<UiState<SimilarMoviesResponseDto>>(UiState.Loading)
    val similarMoviesUiState: StateFlow<UiState<SimilarMoviesResponseDto>> =
        _similarMoviesUiState.asStateFlow()

    private val _movieCreditsUiState =
        MutableStateFlow<UiState<Pair<List<MovieCastDto>, List<MovieCreditsCrewDto>>>>(
            UiState.Loading
        )
    val movieCreditsUiState:
        StateFlow<UiState<Pair<List<MovieCastDto>, List<MovieCreditsCrewDto>>>> =
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
                _movieDetailsUiState.update {
                    when (networkResult) {
                        is NetworkResult.Error -> UiState.Error(networkResult.message)
                        is NetworkResult.Success -> UiState.Success(data = networkResult.data)
                    }
                }
            }
        }
    }

    fun fetchSimilarMovies() {
        viewModelScope.launch {
            _similarMoviesUiState.update { UiState.Loading }

            tmdbMoviesRepository.getSimilarMovies(movieId = movieId).collect { networkResult ->
                _similarMoviesUiState.update {
                    when (networkResult) {
                        is NetworkResult.Error -> UiState.Error(networkResult.message)
                        is NetworkResult.Success -> UiState.Success(data = networkResult.data)
                    }
                }
            }
        }
    }

    fun fetchSimilarMoviesAndCast(similarMovies: SimilarMoviesResponseDto) {
        viewModelScope.launch {
            _movieCreditsUiState.update { UiState.Loading }

            val actors = mutableListOf<MovieCastDto>()
            val directors = mutableListOf<MovieCreditsCrewDto>()

            val deferredResults =
                similarMovies.results.map { movie ->
                    async {
                        movie.id?.let { movieId ->
                            tmdbMoviesRepository.getMovieCredits(movieId).collect { networkResult ->
                                when (networkResult) {
                                    is NetworkResult.Success -> {
                                        actors.addAll(
                                            networkResult.data.movieCastDto.filter {
                                                it.knownForDepartment == "Acting"
                                            }
                                        )
                                        directors.addAll(
                                            networkResult.data.movieCreditsCrewDto.filter {
                                                it.department == "Directing"
                                            }
                                        )
                                    }
                                    is NetworkResult.Error -> Unit
                                }
                            }
                        }
                    }
                }

            deferredResults.awaitAll()

            val sortedActors =
                actors.distinctBy { it.id }.sortedByDescending { it.popularity }.take(5)

            val sortedDirectors =
                directors.distinctBy { it.id }.sortedByDescending { it.popularity }.take(5)

            if (sortedActors.isEmpty() && sortedDirectors.isEmpty()) {
                _movieCreditsUiState.update { UiState.Error("No cast or crew data found") }
            } else {
                _movieCreditsUiState.update { UiState.Success(Pair(sortedActors, sortedDirectors)) }
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
