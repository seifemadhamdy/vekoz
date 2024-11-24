package seifemadhamdy.vekoz.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import seifemadhamdy.vekoz.presentation.ui.navigation.VekozNavHost
import seifemadhamdy.vekoz.presentation.ui.theme.VekozTheme

@AndroidEntryPoint
class VekozActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent { VekozTheme { VekozNavHost(navHostController = rememberNavController()) } }
    }
}
