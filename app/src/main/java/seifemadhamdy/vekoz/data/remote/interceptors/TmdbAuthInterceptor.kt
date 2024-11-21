package seifemadhamdy.vekoz.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import seifemadhamdy.vekoz.BuildConfig
import javax.inject.Inject

class TmdbAuthInterceptor @Inject constructor() : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()

    val requestWithAuth =
        originalRequest
            .newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}")
            .build()

    return chain.proceed(requestWithAuth)
  }
}
