package seifemadhamdy.vekoz.core.constants

sealed class VekozDestination(val route: String) {
  companion object {
    const val MOVIE_ID = "movieId"
  }

  object Home : VekozDestination("home")

  object MovieDetails : VekozDestination("movie_details/{$MOVIE_ID}") {
    fun createRoute(movieId: Int) = "movie_details/$movieId"
  }
}
