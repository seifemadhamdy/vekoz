package seifemadhamdy.vekoz.presentation.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import seifemadhamdy.vekoz.presentation.ui.viewmodels.HomeViewModel

@Composable
fun HomeScreen(navHostController: NavHostController) {
  val homeViewModel: HomeViewModel = hiltViewModel()
}
