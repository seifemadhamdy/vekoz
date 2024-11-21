package seifemadhamdy.vekoz.data.remote.error

import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ApiErrorHandler @Inject constructor() {
  fun handleError(response: Response<*>): NetworkResult.Error =
      NetworkResult.Error(
          code = response.code(),
          message = response.message(),
          errorBody = response.errorBody()?.string(),
      )

  fun handleException(e: Exception): NetworkResult.Error =
      when (e) {
        is NoConnectivityException -> NetworkResult.Error(message = "No internet connection")
        is SocketTimeoutException -> NetworkResult.Error(message = "Connection timeout")
        is UnknownHostException -> NetworkResult.Error(message = "Unable to connect to server")
        else -> NetworkResult.Error(message = e.localizedMessage)
      }
}
