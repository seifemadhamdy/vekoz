package seifemadhamdy.vekoz.presentation.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import seifemadhamdy.vekoz.presentation.ui.viewmodels.MovieDetailsViewModel

@Composable
fun MovieDetailsScreen(navHostController: NavHostController, movieId: Int) {
  val movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
}
