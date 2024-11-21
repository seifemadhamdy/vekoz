package seifemadhamdy.vekoz.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import seifemadhamdy.vekoz.core.utils.NetworkConnectivityManager
import seifemadhamdy.vekoz.data.remote.error.NoConnectivityException
import javax.inject.Inject

class NetworkInterceptor
@Inject
constructor(private val networkConnectivityManager: NetworkConnectivityManager) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    if (!networkConnectivityManager.isNetworkAvailable())
        throw NoConnectivityException("No internet connection")

    val originalRequest = chain.request()

    val requestWithHeaders =
        originalRequest
            .newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()

    return chain.proceed(requestWithHeaders)
  }
}
