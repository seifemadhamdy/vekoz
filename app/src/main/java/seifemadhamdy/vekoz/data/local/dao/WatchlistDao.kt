package seifemadhamdy.vekoz.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import seifemadhamdy.vekoz.data.local.entity.WatchlistEntity

@Dao
interface WatchlistDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addToWatchlist(watchlistEntity: WatchlistEntity)

  @Query("DELETE FROM watchlist WHERE movieId = :movieId") suspend fun deleteByMovieId(movieId: Int)

  @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE movieId = :movieId)")
  suspend fun isInWatchlist(movieId: Int): Boolean
}
