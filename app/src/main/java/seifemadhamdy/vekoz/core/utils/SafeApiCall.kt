package seifemadhamdy.vekoz.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import seifemadhamdy.vekoz.core.utils.exception.ApiException
import seifemadhamdy.vekoz.data.remote.result.NetworkResult
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<NetworkResult<T>> = flow {
    try {
        val response = apiCall()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            emit(NetworkResult.Success(body))
        } else {
            emit(
                NetworkResult.Error(
                    code = response.code(),
                    message = response.message() ?: "Unknown error",
                    errorBody = response.errorBody()?.string(),
                )
            )
        }
    } catch (e: Exception) {
        val networkError =
            when (e) {
                is SocketTimeoutException -> ApiException.Timeout("Request timed out")
                is UnknownHostException -> ApiException.Network("No internet connection")
                is IOException -> ApiException.Network("Network error")
                is HttpException ->
                    when (e.code()) {
                        400 ->
                            ApiException.BadRequest(
                                errorBody = e.response()?.errorBody()?.string(),
                                message = "Invalid request",
                            )
                        else -> ApiException.ServerError(code = e.code(), message = e.message())
                    }
                else -> ApiException.Unknown("Unexpected error")
            }

        emit(
            NetworkResult.Error(
                code = (networkError as? ApiException.ServerError)?.code,
                message = networkError.message!!,
                errorBody = (networkError as? ApiException.BadRequest)?.errorBody,
            )
        )
    }
}
