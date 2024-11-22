package seifemadhamdy.vekoz.domain.models

import kotlinx.coroutines.flow.Flow
import seifemadhamdy.vekoz.data.remote.dto.MoviesByQueryResponseDto
import seifemadhamdy.vekoz.data.remote.result.NetworkResult

interface TmdbSearchRepository {
  suspend fun getMoviesByQuery(query: String): Flow<NetworkResult<MoviesByQueryResponseDto>>
}
