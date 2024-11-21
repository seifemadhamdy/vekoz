package seifemadhamdy.vekoz.data.repositories

import seifemadhamdy.vekoz.core.utils.safeApiCall
import seifemadhamdy.vekoz.data.remote.api.TmdbSearchService
import seifemadhamdy.vekoz.data.remote.dto.MoviesByQueryResponseDto
import seifemadhamdy.vekoz.domain.models.TmdbSearchRepository
import javax.inject.Inject

class TmdbSearchRepositoryImpl
@Inject
constructor(private val tmdbSearchService: TmdbSearchService) : TmdbSearchRepository {

  override suspend fun getMoviesByQuery(query: String): MoviesByQueryResponseDto? {
    return safeApiCall { tmdbSearchService.getMoviesByQuery(query) }
  }
}
