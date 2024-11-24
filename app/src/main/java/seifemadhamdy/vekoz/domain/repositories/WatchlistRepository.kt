package seifemadhamdy.vekoz.domain.repositories

interface WatchlistRepository {
    suspend fun addToWatchlist(movieId: Int)

    suspend fun removeFromWatchlist(movieId: Int)

    suspend fun isInWatchlist(movieId: Int): Boolean
}
