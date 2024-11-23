package seifemadhamdy.vekoz.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import seifemadhamdy.vekoz.presentation.ui.viewmodels.MovieDetailsViewModel
import seifemadhamdy.vekoz.presentation.ui.viewmodels.MovieDetailsViewModel.Companion.UiState

@Composable
fun MovieDetailsScreen(navHostController: NavHostController, movieId: Int) {
  val movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
  val movieDetailsUiState by movieDetailsViewModel.movieDetailsUiState.collectAsState()
  val similarMoviesUiState by movieDetailsViewModel.similarMoviesUiState.collectAsState()
  val movieCreditsUiState by movieDetailsViewModel.movieCreditsUiState.collectAsState()

  when (movieDetailsUiState) {
    is UiState.Error -> {}
    UiState.Loading -> {}
    is UiState.Success -> {
      Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        when (similarMoviesUiState) {
          is UiState.Error -> {}
          UiState.Loading -> {}
          is UiState.Success -> {}
        }

        when (movieCreditsUiState) {
          is UiState.Error -> {}
          UiState.Loading -> {}
          is UiState.Success -> {}
        }
      }
    }
  }
}
