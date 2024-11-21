package seifemadhamdy.vekoz.domain.models

import seifemadhamdy.vekoz.data.remote.dto.MoviesByQueryResponseDto

interface TmdbSearchRepository {
  suspend fun getMoviesByQuery(query: String): MoviesByQueryResponseDto?
}
