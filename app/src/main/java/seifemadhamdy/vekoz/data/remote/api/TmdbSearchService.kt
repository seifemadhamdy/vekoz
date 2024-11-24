package seifemadhamdy.vekoz.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import seifemadhamdy.vekoz.data.remote.dto.MoviesByQueryResponseDto

interface TmdbSearchService {
    @GET("search/movie")
    suspend fun getMoviesByQuery(@Query("query") query: String): Response<MoviesByQueryResponseDto>
}
