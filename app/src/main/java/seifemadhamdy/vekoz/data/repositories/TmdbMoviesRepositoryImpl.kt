package seifemadhamdy.vekoz.data.repositories

import seifemadhamdy.vekoz.core.utils.safeApiCall
import seifemadhamdy.vekoz.data.remote.api.TmdbMoviesService
import seifemadhamdy.vekoz.data.remote.dto.*
import seifemadhamdy.vekoz.domain.models.TmdbMoviesRepository
import javax.inject.Inject

class TmdbMoviesRepositoryImpl
@Inject
constructor(private val tmdbMoviesService: TmdbMoviesService) : TmdbMoviesRepository {

  override suspend fun getPopularMovies(): PopularMoviesResponseDto? {
    return safeApiCall { tmdbMoviesService.getPopularMovies() }
  }

  override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponseDto? {
    return safeApiCall { tmdbMoviesService.getMovieDetails(movieId) }
  }

  override suspend fun getSimilarMovies(movieId: Int): SimilarMoviesResponseDto? {
    return safeApiCall { tmdbMoviesService.getSimilarMovies(movieId) }
  }

  override suspend fun getMovieCredits(movieId: Int): MovieCreditsResponseDto? {
    return safeApiCall { tmdbMoviesService.getMovieCredits(movieId) }
  }
}
