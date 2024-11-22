package seifemadhamdy.vekoz.domain.models

import kotlinx.coroutines.flow.Flow
import seifemadhamdy.vekoz.data.remote.dto.MovieCreditsResponseDto
import seifemadhamdy.vekoz.data.remote.dto.MovieDetailsResponseDto
import seifemadhamdy.vekoz.data.remote.dto.PopularMoviesResponseDto
import seifemadhamdy.vekoz.data.remote.dto.SimilarMoviesResponseDto
import seifemadhamdy.vekoz.data.remote.result.NetworkResult

interface TmdbMoviesRepository {
  suspend fun getPopularMovies(): Flow<NetworkResult<PopularMoviesResponseDto>>

  suspend fun getMovieDetails(movieId: Int): Flow<NetworkResult<MovieDetailsResponseDto>>

  suspend fun getSimilarMovies(movieId: Int): Flow<NetworkResult<SimilarMoviesResponseDto>>

  suspend fun getMovieCredits(movieId: Int): Flow<NetworkResult<MovieCreditsResponseDto>>
}
