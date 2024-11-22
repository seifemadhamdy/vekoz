package seifemadhamdy.vekoz.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import seifemadhamdy.vekoz.data.local.dao.WatchlistDao
import seifemadhamdy.vekoz.data.local.db.WatchlistDatabase
import seifemadhamdy.vekoz.data.repositories.WatchlistRepositoryImpl
import seifemadhamdy.vekoz.domain.repositories.WatchlistRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
  @Provides
  @Singleton
  fun provideWatchlistDatabase(@ApplicationContext context: Context): WatchlistDatabase =
      Room.databaseBuilder(context, WatchlistDatabase::class.java, "watchlist_database").build()

  @Provides
  @Singleton
  fun provideWatchlistDao(database: WatchlistDatabase): WatchlistDao = database.watchlistDao()

  @Provides
  @Singleton
  fun provideWatchlistRepository(watchlistDao: WatchlistDao): WatchlistRepository =
      WatchlistRepositoryImpl(watchlistDao)
}
