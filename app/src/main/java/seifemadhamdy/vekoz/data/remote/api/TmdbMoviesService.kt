package seifemadhamdy.vekoz.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import seifemadhamdy.vekoz.data.remote.dto.MovieCreditsResponseDto
import seifemadhamdy.vekoz.data.remote.dto.MovieDetailsResponseDto
import seifemadhamdy.vekoz.data.remote.dto.PopularMoviesResponseDto
import seifemadhamdy.vekoz.data.remote.dto.SimilarMoviesResponseDto

interface TmdbMoviesService {
  @GET("movie/popular") suspend fun getPopularMovies(): Response<PopularMoviesResponseDto>

  @GET("movie/{movie_id}")
  suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<MovieDetailsResponseDto>

  @GET("movie/{movie_id}/similar")
  suspend fun getSimilarMovies(@Path("movie_id") movieId: Int): Response<SimilarMoviesResponseDto>

  @GET("movie/{movie_id}/credits")
  suspend fun getMovieCredits(@Path("movie_id") movieId: Int): Response<MovieCreditsResponseDto>
}
