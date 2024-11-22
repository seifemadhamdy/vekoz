package seifemadhamdy.vekoz.data.repositories

import kotlinx.coroutines.flow.Flow
import seifemadhamdy.vekoz.core.utils.safeApiCall
import seifemadhamdy.vekoz.data.remote.api.TmdbMoviesService
import seifemadhamdy.vekoz.data.remote.dto.*
import seifemadhamdy.vekoz.data.remote.result.NetworkResult
import seifemadhamdy.vekoz.domain.models.TmdbMoviesRepository
import javax.inject.Inject

class TmdbMoviesRepositoryImpl
@Inject
constructor(private val tmdbMoviesService: TmdbMoviesService) : TmdbMoviesRepository {

  override suspend fun getPopularMovies(): Flow<NetworkResult<PopularMoviesResponseDto>> =
      safeApiCall {
        tmdbMoviesService.getPopularMovies()
      }

  override suspend fun getMovieDetails(movieId: Int): Flow<NetworkResult<MovieDetailsResponseDto>> =
      safeApiCall {
        tmdbMoviesService.getMovieDetails(movieId)
      }

  override suspend fun getSimilarMovies(
      movieId: Int
  ): Flow<NetworkResult<SimilarMoviesResponseDto>> = safeApiCall {
    tmdbMoviesService.getSimilarMovies(movieId)
  }

  override suspend fun getMovieCredits(movieId: Int): Flow<NetworkResult<MovieCreditsResponseDto>> =
      safeApiCall {
        tmdbMoviesService.getMovieCredits(movieId)
      }
}
