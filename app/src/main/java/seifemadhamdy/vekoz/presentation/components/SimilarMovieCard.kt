package seifemadhamdy.vekoz.presentation.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import seifemadhamdy.vekoz.core.constants.TmdbUrls
import seifemadhamdy.vekoz.data.remote.dto.ResultsDto
import seifemadhamdy.vekoz.presentation.ui.navigation.VekozDestination

@Composable
fun SimilarMovieCard(movieItemData: ResultsDto, navHostController: NavHostController) {
    Card(
        onClick = {
            movieItemData.id?.let { id ->
                navHostController.navigate(
                    route = VekozDestination.MovieDetails.createRoute(movieId = id)
                )
            }
        },
        modifier =
            Modifier.width((LocalConfiguration.current.screenWidthDp.dp / 3) - 26.dp)
                .aspectRatio(0.6666667f),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        AsyncImage(
            model = "${TmdbUrls.BASE_IMAGE_URL}/original${movieItemData.posterPath}",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}
