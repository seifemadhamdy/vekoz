package seifemadhamdy.vekoz.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import seifemadhamdy.vekoz.presentation.ui.theme.VekozTheme

@AndroidEntryPoint
class VekozActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    enableEdgeToEdge()
    setContent {
      VekozTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          // TODO: Design goes here.
        }
      }
    }
  }
}
