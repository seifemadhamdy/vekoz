package seifemadhamdy.vekoz.data.repositories

import seifemadhamdy.vekoz.data.local.dao.WatchlistDao
import seifemadhamdy.vekoz.data.local.entity.WatchlistEntity
import seifemadhamdy.vekoz.domain.repositories.WatchlistRepository
import javax.inject.Inject

class WatchlistRepositoryImpl @Inject constructor(private val watchlistDao: WatchlistDao) :
    WatchlistRepository {

  override suspend fun addToWatchlist(movieId: Int) {
    watchlistDao.addToWatchlist(WatchlistEntity(movieId = movieId))
  }

  override suspend fun removeFromWatchlist(movieId: Int) {
    watchlistDao.deleteByMovieId(movieId)
  }

  override suspend fun isInWatchlist(movieId: Int): Boolean {
    return watchlistDao.isInWatchlist(movieId)
  }
}
