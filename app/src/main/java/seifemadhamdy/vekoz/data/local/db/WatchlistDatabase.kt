package seifemadhamdy.vekoz.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import seifemadhamdy.vekoz.data.local.dao.WatchlistDao
import seifemadhamdy.vekoz.data.local.entity.WatchlistEntity

@Database(entities = [WatchlistEntity::class], version = 1)
abstract class WatchlistDatabase : RoomDatabase() {
  abstract fun watchlistDao(): WatchlistDao
}
