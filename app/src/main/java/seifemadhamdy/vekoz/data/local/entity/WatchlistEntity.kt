package seifemadhamdy.vekoz.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist") data class WatchlistEntity(@PrimaryKey val movieId: Int)
