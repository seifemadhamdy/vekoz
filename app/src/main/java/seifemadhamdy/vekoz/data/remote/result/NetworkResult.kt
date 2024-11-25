package seifemadhamdy.vekoz.data.remote.result

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()

    data class Error(val code: Int? = null, val message: String, val errorBody: String? = null) :
        NetworkResult<Nothing>()
}
