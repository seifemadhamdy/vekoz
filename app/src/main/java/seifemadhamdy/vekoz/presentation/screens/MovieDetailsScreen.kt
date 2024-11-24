package seifemadhamdy.vekoz.presentation.screens

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import seifemadhamdy.vekoz.R
import seifemadhamdy.vekoz.core.constants.TmdbUrls
import seifemadhamdy.vekoz.data.remote.dto.MovieCastDto
import seifemadhamdy.vekoz.data.remote.dto.MovieCreditsCrewDto
import seifemadhamdy.vekoz.data.remote.dto.MovieDetailsResponseDto
import seifemadhamdy.vekoz.data.remote.dto.ResultsDto
import seifemadhamdy.vekoz.presentation.components.ExpandableText
import seifemadhamdy.vekoz.presentation.components.Person
import seifemadhamdy.vekoz.presentation.components.PersonCard
import seifemadhamdy.vekoz.presentation.components.SimilarMovieCard
import seifemadhamdy.vekoz.presentation.ui.viewmodels.MovieDetailsViewModel
import seifemadhamdy.vekoz.presentation.ui.viewmodels.MovieDetailsViewModel.Companion.UiState
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(navHostController: NavHostController) {
  val movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
  val movieDetailsUiState by movieDetailsViewModel.movieDetailsUiState.collectAsState()

  when (movieDetailsUiState) {
    is UiState.Error -> {}
    UiState.Loading -> {}
    is UiState.Success -> {
      val movieDetailsData = (movieDetailsUiState as UiState.Success<MovieDetailsResponseDto>).data

      Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {
            Row(
                modifier =
                    Modifier.fillMaxWidth()
                        .windowInsetsPadding(TopAppBarDefaults.windowInsets)
                        .padding(all = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                  Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                    Text(
                        text = movieDetailsData.title ?: "Unknown Title",
                        modifier = Modifier.fillMaxWidth().basicMarquee(),
                        color = colorScheme.onSurface,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleLarge)

                    movieDetailsData.releaseDate?.let { releaseDate ->
                      if (releaseDate.isNotBlank())
                          Text(
                              text =
                                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                      LocalDate.parse(releaseDate)
                                          .format(
                                              DateTimeFormatter.ofPattern(
                                                  "d MMM, yyyy", Locale.ENGLISH))
                                  else releaseDate,
                              modifier = Modifier.fillMaxWidth().alpha(alpha = 0.6f).basicMarquee(),
                              color = colorScheme.onSurface,
                              maxLines = 1,
                              style = MaterialTheme.typography.labelLarge)
                    }
                  }

                  movieDetailsData.id?.let { id ->
                    val isInWatchlist by movieDetailsViewModel.isMovieInWatchlist.collectAsState()

                    FilledIconToggleButton(
                        checked = isInWatchlist,
                        onCheckedChange = {
                          if (isInWatchlist) movieDetailsViewModel.removeMovieFromWatchlist()
                          else movieDetailsViewModel.addMovieToWatchlist()
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
          }) { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = rememberLazyListState(),
                contentPadding =
                    PaddingValues(bottom = paddingValues.calculateBottomPadding() + 16.dp)) {
                  item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                      AsyncImage(
                          model =
                              "${TmdbUrls.BASE_IMAGE_URL}/original${movieDetailsData.backdropPath}",
                          contentDescription = null,
                          modifier =
                              Modifier.fillMaxWidth()
                                  .aspectRatio(ratio = 1.7777778f)
                                  .graphicsLayer(
                                      compositingStrategy = CompositingStrategy.Offscreen)
                                  .drawWithContent {
                                    drawContent()
                                    drawRect(
                                        brush =
                                            Brush.verticalGradient(
                                                listOf(Color.Black, Color.Transparent)),
                                        blendMode = BlendMode.DstIn)
                                  },
                          contentScale = ContentScale.Crop)

                      val layoutDirection = LocalLayoutDirection.current

                      Column(
                          modifier =
                              Modifier.fillMaxWidth()
                                  .padding(
                                      start =
                                          paddingValues.calculateStartPadding(
                                              layoutDirection = layoutDirection),
                                      top = paddingValues.calculateTopPadding() + 16.dp,
                                      end =
                                          paddingValues.calculateEndPadding(
                                              layoutDirection = layoutDirection) + 16.dp),
                          verticalArrangement = Arrangement.spacedBy(16.dp),
                          horizontalAlignment = Alignment.CenterHorizontally) {
                            val goldenRatioComplementary = 0.381966012f

                            ElevatedCard(
                                modifier =
                                    Modifier.fillMaxWidth(fraction = goldenRatioComplementary)
                                        .aspectRatio(0.6666667f),
                                shape = MaterialTheme.shapes.extraLarge,
                                elevation =
                                    CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)) {
                                  AsyncImage(
                                      model =
                                          "${TmdbUrls.BASE_IMAGE_URL}/original${movieDetailsData.posterPath}",
                                      contentDescription = null,
                                      modifier =
                                          Modifier.fillMaxWidth().aspectRatio(ratio = 0.6666667f),
                                      contentScale = ContentScale.Crop)
                                }
                          }
                    }
                  }

                  item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                          movieDetailsData.tagline?.let { tagline ->
                            Text(
                                text = tagline,
                                modifier = Modifier.fillMaxWidth().alpha(alpha = 0.87f),
                                color = colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium)
                          }

                          val genres = movieDetailsData.genres

                          if (genres.isNotEmpty())
                              Text(
                                  text = genres.map { it.name }.joinToString(separator = " | "),
                                  modifier = Modifier.fillMaxWidth().alpha(alpha = 0.6f),
                                  color = colorScheme.onSurface,
                                  textAlign = TextAlign.Center,
                                  style = MaterialTheme.typography.labelLarge)
                        }
                  }

                  item {
                    val attributes =
                        mutableMapOf<Int, Any?>().apply {
                          put(
                              R.drawable.ic_star_fill,
                              "%.1f".format((movieDetailsData.voteAverage?.div(10.0))?.times(5)))
                          put(R.drawable.ic_strategy_fill, movieDetailsData.status)
                          put(
                              R.drawable.ic_money_fill,
                              formatAsCurrency(value = movieDetailsData.revenue))
                        }

                    Row(
                        modifier =
                            Modifier.fillMaxWidth()
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically) {
                          attributes.forEach { (iconResId, value) ->
                            value?.let { value ->
                              Row(
                                  modifier =
                                      Modifier.background(
                                              color = colorScheme.surfaceContainer,
                                              shape = MaterialTheme.shapes.extraLarge)
                                          .padding(horizontal = 16.dp, vertical = 8.dp),
                                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                                  verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = iconResId),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = colorScheme.onSurface)

                                    Text(
                                        text = value.toString(),
                                        modifier = Modifier.alpha(alpha = 0.6f),
                                        color = colorScheme.onSurface,
                                        style = MaterialTheme.typography.labelLarge)
                                  }
                            }
                          }
                        }
                  }

                  item {
                    movieDetailsData.overview?.let { overview ->
                      ExpandableText(
                          text = overview,
                          modifier =
                              Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                                  .fillMaxWidth()
                                  .alpha(alpha = 0.87f),
                          color = colorScheme.onSurface,
                          textAlign = TextAlign.Center,
                          maxLinesBeforeExpansion = 3,
                          style = MaterialTheme.typography.bodyMedium)
                    }
                  }

                  item {
                    LaunchedEffect(Unit) { movieDetailsViewModel.fetchSimilarMovies() }

                    val similarMoviesUiState by
                        movieDetailsViewModel.similarMoviesUiState.collectAsState()

                    when (similarMoviesUiState) {
                      is UiState.Error -> {}
                      UiState.Loading -> {}
                      is UiState.Success -> {
                        Text(
                            text = "More Like This",
                            modifier =
                                Modifier.fillMaxWidth()
                                    .alpha(alpha = 0.87f)
                                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            color = colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge)

                        val fiveSimilarMoviesData =
                            (similarMoviesUiState as UiState.Success<List<ResultsDto>>).data.take(5)

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            state = rememberLazyListState(),
                            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                              items(
                                  items = fiveSimilarMoviesData,
                                  key = {
                                    it.id ?: "item_at_${fiveSimilarMoviesData.indexOf(it)}"
                                  }) { movie ->
                                    SimilarMovieCard(
                                        navHostController = navHostController,
                                        movieItemData = movie)
                                  }
                            }
                      }
                    }
                  }

                  item {
                    LaunchedEffect(Unit) { movieDetailsViewModel.fetchSimilarMoviesAndCast() }

                    val movieCreditsUiState by
                        movieDetailsViewModel.movieCreditsUiState.collectAsState()

                    when (movieCreditsUiState) {
                      is UiState.Error -> {}
                      UiState.Loading -> {}
                      is UiState.Success -> {
                        Text(
                            text = "Leading Stars of Similar Films",
                            modifier =
                                Modifier.fillMaxWidth()
                                    .alpha(alpha = 0.87f)
                                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            color = colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge)

                        val actorsData =
                            (movieCreditsUiState
                                    as
                                    UiState.Success<
                                        Pair<List<MovieCastDto>, List<MovieCreditsCrewDto>>>)
                                .data
                                .first

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            state = rememberLazyListState(),
                            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                              items(
                                  items = actorsData,
                                  key = { it.id ?: "item_at_${actorsData.indexOf(it)}" }) { actor ->
                                    PersonCard(person = Person.Actor(actor))
                                  }
                            }

                        Text(
                            text = "Top Directors Behind Similar Films",
                            modifier =
                                Modifier.fillMaxWidth()
                                    .alpha(alpha = 0.87f)
                                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            color = colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge)

                        val directorsData =
                            (movieCreditsUiState
                                    as
                                    UiState.Success<
                                        Pair<List<MovieCastDto>, List<MovieCreditsCrewDto>>>)
                                .data
                                .second

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            state = rememberLazyListState(),
                            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                              items(
                                  items = directorsData,
                                  key = { it.id ?: "item_at_${directorsData.indexOf(it)}" }) {
                                      director ->
                                    PersonCard(person = Person.Director(director))
                                  }
                            }
                      }
                    }
                  }
                }
          }
    }
  }
}

fun formatAsCurrency(value: Int?): String? {
  return if (value != null) {
    NumberFormat.getCurrencyInstance(Locale.US).run {
      maximumFractionDigits = 0
      format(value)
    }
  } else {
    null
  }
}
