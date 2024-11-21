package seifemadhamdy.vekoz.domain.models

import seifemadhamdy.vekoz.data.remote.dto.MovieCreditsResponseDto
import seifemadhamdy.vekoz.data.remote.dto.MovieDetailsResponseDto
import seifemadhamdy.vekoz.data.remote.dto.PopularMoviesResponseDto
import seifemadhamdy.vekoz.data.remote.dto.SimilarMoviesResponseDto

interface TmdbMoviesRepository {
  suspend fun getPopularMovies(): PopularMoviesResponseDto?

  suspend fun getMovieDetails(movieId: Int): MovieDetailsResponseDto?

  suspend fun getSimilarMovies(movieId: Int): SimilarMoviesResponseDto?

  suspend fun getMovieCredits(movieId: Int): MovieCreditsResponseDto?
}
