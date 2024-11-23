package seifemadhamdy.vekoz.presentation.components

import android.os.Build
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import seifemadhamdy.vekoz.R
import seifemadhamdy.vekoz.core.constants.TmdbUrls
import seifemadhamdy.vekoz.data.remote.dto.ResultsDto
import seifemadhamdy.vekoz.presentation.ui.navigation.VekozDestination
import seifemadhamdy.vekoz.presentation.ui.viewmodels.HomeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MovieCard(
    movieItemData: ResultsDto,
    navHostController: NavHostController,
    viewModel: HomeViewModel
) {
  Card(
      onClick = {
        movieItemData.id?.let { id ->
          navHostController.navigate(
              route = VekozDestination.MovieDetails.createRoute(movieId = id))
        }
      },
      modifier = Modifier.fillMaxWidth().aspectRatio(ratio = 0.6666667f),
      shape = MaterialTheme.shapes.extraLarge) {
        Box(modifier = Modifier.fillMaxSize()) {
          val hazeState = remember { HazeState() }

          AsyncImage(
              model = "${TmdbUrls.BASE_IMAGE_URL}/original${movieItemData.posterPath}",
              contentDescription = null,
              modifier = Modifier.fillMaxSize().haze(state = hazeState),
              contentScale = ContentScale.Crop)

          Column(
              modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            val hazeTint = HazeTint(color = colorScheme.surface.copy(alpha = 0.6f))

            val hazeStyle =
                HazeStyle(
                    backgroundColor = Color.Black,
                    tint = hazeTint,
                    blurRadius = 20.dp,
                    noiseFactor = 0.0625f,
                    fallbackTint = hazeTint)

            movieItemData.voteAverage?.let { voteAverage ->
              if (voteAverage != 0.0)
                  Row(
                      modifier =
                          Modifier.padding(all = 16.dp)
                              .clip(shape = MaterialTheme.shapes.extraLarge)
                              .hazeChild(state = hazeState, style = hazeStyle)
                              .padding(horizontal = 16.dp, vertical = 8.dp),
                      horizontalArrangement = Arrangement.spacedBy(8.dp),
                      verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_star_fill),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = colorScheme.onSurface)

                        Text(
                            text = "%.1f".format((voteAverage / 10.0) * 5),
                            modifier = Modifier.alpha(alpha = 0.6f),
                            color = colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge)
                      }
            }

            Spacer(modifier = Modifier.weight(weight = 1f))

            Column(
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(all = 16.dp)
                        .clip(shape = MaterialTheme.shapes.extraLarge)
                        .hazeChild(state = hazeState, style = hazeStyle)
                        .padding(start = 16.dp, top = 8.dp, end = 0.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Row(
                      modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                      horizontalArrangement = Arrangement.spacedBy(8.dp),
                      verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                          movieItemData.title?.let { title ->
                            Text(
                                text = title,
                                color = colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth().basicMarquee(),
                                maxLines = 1,
                                style = MaterialTheme.typography.titleMedium)
                          }

                          movieItemData.releaseDate?.let { releaseDate ->
                            if (releaseDate.isNotBlank())
                                Text(
                                    text =
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                            LocalDate.parse(releaseDate)
                                                .format(
                                                    DateTimeFormatter.ofPattern(
                                                        "d MMM, yyyy", Locale.ENGLISH))
                                        else releaseDate,
                                    modifier =
                                        Modifier.fillMaxWidth().alpha(alpha = 0.6f).basicMarquee(),
                                    color = colorScheme.onSurface,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.labelLarge)
                          }
                        }

                        movieItemData.id?.let { id ->
                          LaunchedEffect(id) { viewModel.initializeWatchlistState(id) }
                          val isInWatchlist by viewModel.isMovieInWatchlist(id).collectAsState()

                          FilledIconToggleButton(
                              checked = isInWatchlist,
                              onCheckedChange = {
                                if (isInWatchlist) viewModel.removeMovieFromWatchlist(movieId = id)
                                else viewModel.addMovieToWatchlist(movieId = id)
                              },
                              colors =
                                  IconButtonDefaults.filledIconToggleButtonColors(
                                      containerColor = colorScheme.surfaceVariant,
                                      contentColor = colorScheme.onSurfaceVariant,
                                      checkedContainerColor = colorScheme.primaryContainer,
                                      checkedContentColor = colorScheme.onPrimaryContainer)) {
                                Icon(
                                    painter =
                                        painterResource(
                                            if (!isInWatchlist) R.drawable.ic_eye_slash_bold
                                            else R.drawable.ic_eye_fill),
                                    contentDescription = null)
                              }
                        }
                      }

                  movieItemData.overview?.let { overview ->
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())

                    ExpandableText(
                        text = overview,
                        modifier =
                            Modifier.padding(end = 16.dp).fillMaxWidth().alpha(alpha = 0.87f),
                        color = colorScheme.onSurface,
                        maxLinesBeforeExpansion = 1,
                        style = MaterialTheme.typography.bodyMedium)
                  }
                }
          }
        }
      }
}
