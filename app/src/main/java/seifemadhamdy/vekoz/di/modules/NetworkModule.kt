package seifemadhamdy.vekoz.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import seifemadhamdy.vekoz.core.constants.TmdbConstants
import seifemadhamdy.vekoz.data.remote.api.TmdbMoviesService
import seifemadhamdy.vekoz.data.remote.api.TmdbSearchService
import seifemadhamdy.vekoz.data.remote.interceptors.NetworkInterceptor
import seifemadhamdy.vekoz.data.remote.interceptors.TmdbAuthInterceptor
import seifemadhamdy.vekoz.data.repositories.TmdbMoviesRepositoryImpl
import seifemadhamdy.vekoz.data.repositories.TmdbSearchRepositoryImpl
import seifemadhamdy.vekoz.domain.models.TmdbMoviesRepository
import seifemadhamdy.vekoz.domain.models.TmdbSearchRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
  @Provides
  @Singleton
  fun provideTmdbOkHttpClient(
      networkInterceptor: NetworkInterceptor,
      tmdbAuthInterceptor: TmdbAuthInterceptor,
  ): OkHttpClient =
      OkHttpClient.Builder()
          .addInterceptor(networkInterceptor)
          .addInterceptor(tmdbAuthInterceptor)
          .build()

  @Provides
  @Singleton
  fun provideTmdbRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(TmdbConstants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
  }

  @Provides
  @Singleton
  fun provideTmdbMoviesService(retrofit: Retrofit): TmdbMoviesService {
    return retrofit.create(TmdbMoviesService::class.java)
  }

  @Provides
  @Singleton
  fun provideTmdbSearchService(retrofit: Retrofit): TmdbSearchService {
    return retrofit.create(TmdbSearchService::class.java)
  }

  @Provides
  @Singleton
  fun provideTmdbMoviesRepository(tmdbMoviesService: TmdbMoviesService): TmdbMoviesRepository {
    return TmdbMoviesRepositoryImpl(tmdbMoviesService)
  }

  @Provides
  @Singleton
  fun provideTmdbSearchRepository(tmdbSearchService: TmdbSearchService): TmdbSearchRepository {
    return TmdbSearchRepositoryImpl(tmdbSearchService)
  }
}
