package seifemadhamdy.vekoz.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import seifemadhamdy.vekoz.core.constants.TmdbUrls
import seifemadhamdy.vekoz.data.remote.api.TmdbMoviesService
import seifemadhamdy.vekoz.data.remote.api.TmdbSearchService
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
      tmdbAuthInterceptor: TmdbAuthInterceptor,
  ): OkHttpClient = OkHttpClient.Builder().addInterceptor(tmdbAuthInterceptor).build()

  @Provides
  @Singleton
  fun provideTmdbRetrofit(okHttpClient: OkHttpClient): Retrofit =
      Retrofit.Builder()
          .baseUrl(TmdbUrls.BASE_API_URL)
          .client(okHttpClient)
          .addConverterFactory(GsonConverterFactory.create())
          .build()

  @Provides
  @Singleton
  fun provideTmdbMoviesService(retrofit: Retrofit): TmdbMoviesService =
      retrofit.create(TmdbMoviesService::class.java)

  @Provides
  @Singleton
  fun provideTmdbSearchService(retrofit: Retrofit): TmdbSearchService =
      retrofit.create(TmdbSearchService::class.java)

  @Provides
  @Singleton
  fun provideTmdbMoviesRepository(tmdbMoviesService: TmdbMoviesService): TmdbMoviesRepository =
      TmdbMoviesRepositoryImpl(tmdbMoviesService)

  @Provides
  @Singleton
  fun provideTmdbSearchRepository(tmdbSearchService: TmdbSearchService): TmdbSearchRepository =
      TmdbSearchRepositoryImpl(tmdbSearchService)
}
