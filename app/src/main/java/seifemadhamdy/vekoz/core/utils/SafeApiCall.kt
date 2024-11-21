package seifemadhamdy.vekoz.core.utils

import retrofit2.Response

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): T? {
  return runCatching { apiCall() }.getOrNull()?.takeIf { it.isSuccessful }?.body()
}
