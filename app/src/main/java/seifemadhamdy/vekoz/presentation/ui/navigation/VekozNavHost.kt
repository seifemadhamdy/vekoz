package seifemadhamdy.vekoz.presentation.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import seifemadhamdy.vekoz.presentation.screens.HomeScreen
import seifemadhamdy.vekoz.presentation.screens.MovieDetailsScreen

@Composable
fun VekozNavHost(
    navHostController: NavHostController,
    startDestination: String = VekozDestination.Home.route
) {
  NavHost(
      navController = navHostController,
      startDestination = startDestination,
      modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        composable(route = VekozDestination.Home.route) {
          HomeScreen(navHostController = navHostController)
        }

        composable(
            route = VekozDestination.MovieDetails.route,
            arguments =
                listOf(navArgument(VekozDestination.MOVIE_ID) { type = NavType.IntType })) {
                navBackStackEntry ->
              navBackStackEntry.arguments?.getInt(VekozDestination.MOVIE_ID)?.let { movieId ->
                MovieDetailsScreen(navHostController = navHostController, movieId = movieId)
              }
            }
      }
}
