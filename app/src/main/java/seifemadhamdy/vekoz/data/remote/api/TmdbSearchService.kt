package seifemadhamdy.vekoz.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import seifemadhamdy.vekoz.data.remote.dto.MoviesByQueryResponseDto

interface TmdbSearchService {
  @GET("search/movie?query={query}")
  suspend fun getMoviesByQuery(@Path("query") query: String): Response<MoviesByQueryResponseDto>
}
