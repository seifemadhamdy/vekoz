package seifemadhamdy.vekoz.core.utils.exception

sealed class ApiException : Exception() {
  data class Network(override val message: String) : ApiException()

  data class Timeout(override val message: String) : ApiException()

  data class Unknown(override val message: String) : ApiException()

  data class ServerError(val code: Int, override val message: String) : ApiException()

  data class BadRequest(val errorBody: String?, override val message: String) : ApiException()
}
