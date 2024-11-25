package seifemadhamdy.vekoz.presentation.screens

import android.widget.Toast
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import seifemadhamdy.vekoz.R
import seifemadhamdy.vekoz.core.constants.TmdbUrls
import seifemadhamdy.vekoz.core.utils.StringFormatter
import seifemadhamdy.vekoz.presentation.components.AttributeSurface
import seifemadhamdy.vekoz.presentation.components.ExpandableText
import seifemadhamdy.vekoz.presentation.components.IllustrationBox
import seifemadhamdy.vekoz.presentation.components.Person
import seifemadhamdy.vekoz.presentation.components.PersonCard
import seifemadhamdy.vekoz.presentation.components.SimilarMovieCard
import seifemadhamdy.vekoz.presentation.ui.viewmodels.MovieDetailsViewModel
import seifemadhamdy.vekoz.presentation.ui.viewmodels.MovieDetailsViewModel.Companion.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(navHostController: NavHostController) {
    val movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
    val movieDetailsUiState by movieDetailsViewModel.movieDetailsUiState.collectAsState()

    Crossfade(targetState = movieDetailsUiState, label = "movieDetailsUiStateCrossfade") { uiState
        ->
        when (uiState) {
            is UiState.Error -> {
                val backgroundColor = colorScheme.errorContainer

                Box(
                    modifier =
                        Modifier.fillMaxSize()
                            .background(
                                brush =
                                    Brush.verticalGradient(
                                        colors = listOf(backgroundColor, Color.Transparent)
                                    )
                            )
                            .safeDrawingPadding()
                            .padding(all = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    IllustrationBox(
                        imageResId = R.drawable.ic_warning_fill,
                        title = stringResource(R.string.plot_twist),
                        body = uiState.message,
                        contentColor = contentColorFor(backgroundColor),
                    ) {
                        Button(onClick = { movieDetailsViewModel.fetchMovieDetails() }) {
                            Text(stringResource(R.string.reload))
                        }
                    }
                }
            }

            UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().safeDrawingPadding(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(modifier = Modifier.width(48.dp))
                }
            }
            is UiState.Success -> {
                val movieDetailsData = uiState.data

                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    val layoutDirection = LocalLayoutDirection.current

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = rememberLazyListState(),
                        contentPadding =
                            PaddingValues(
                                start = paddingValues.calculateStartPadding(layoutDirection),
                                end = paddingValues.calculateEndPadding(layoutDirection),
                                bottom = paddingValues.calculateBottomPadding() + 16.dp,
                            ),
                    ) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier =
                                        Modifier.fillMaxWidth()
                                            .aspectRatio(1.7777778f)
                                            .graphicsLayer(
                                                compositingStrategy = CompositingStrategy.Offscreen
                                            )
                                            .drawWithContent {
                                                drawContent()

                                                drawRect(
                                                    brush =
                                                        Brush.verticalGradient(
                                                            listOf(Color.Black, Color.Transparent)
                                                        ),
                                                    blendMode = BlendMode.DstIn,
                                                )
                                            }
                                ) {
                                    val hazeState = remember { HazeState() }

                                    AsyncImage(
                                        model =
                                            "${TmdbUrls.BASE_IMAGE_URL}/original${movieDetailsData.backdropPath}",
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize().haze(hazeState),
                                        contentScale = ContentScale.Crop,
                                    )

                                    val hazeStyle =
                                        HazeTint(
                                                color =
                                                    colorScheme.surfaceContainer.copy(alpha = 0.87f)
                                            )
                                            .run {
                                                HazeStyle(
                                                    backgroundColor = Color.Black,
                                                    tint = this,
                                                    blurRadius = 20.dp,
                                                    noiseFactor = 0f,
                                                    fallbackTint = this,
                                                )
                                            }

                                    Box(
                                        modifier =
                                            Modifier.fillMaxSize().hazeChild(
                                                state = hazeState,
                                                style = hazeStyle,
                                            ) {
                                                progressive =
                                                    HazeProgressive.verticalGradient(
                                                        startIntensity = 1f,
                                                        endIntensity = 0f,
                                                        preferPerformance = true,
                                                    )
                                            }
                                    )
                                }

                                Column(
                                    modifier =
                                        Modifier.fillMaxWidth()
                                            .padding(
                                                top = paddingValues.calculateTopPadding() + 16.dp
                                            ),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        movieDetailsData.title?.let { title ->
                                            Text(
                                                text = title,
                                                modifier =
                                                    Modifier.fillMaxWidth()
                                                        .padding(horizontal = 16.dp)
                                                        .basicMarquee(),
                                                color = colorScheme.onSurface,
                                                maxLines = 1,
                                                textAlign = TextAlign.Center,
                                                style = typography.titleLarge,
                                            )
                                        }

                                        movieDetailsData.tagline?.let { tagline ->
                                            Text(
                                                text = tagline,
                                                modifier =
                                                    Modifier.fillMaxWidth()
                                                        .padding(horizontal = 16.dp)
                                                        .alpha(alpha = 0.6f),
                                                color = colorScheme.onSurface,
                                                maxLines = 1,
                                                textAlign = TextAlign.Center,
                                                style = typography.bodyLarge,
                                            )
                                        }
                                    }

                                    ElevatedCard(
                                        modifier =
                                            Modifier.fillMaxWidth(fraction = 0.5f)
                                                .aspectRatio(0.6666667f),
                                        shape = shapes.extraLarge,
                                        elevation =
                                            CardDefaults.elevatedCardElevation(
                                                defaultElevation = 6.dp
                                            ),
                                    ) {
                                        AsyncImage(
                                            model =
                                                "${TmdbUrls.BASE_IMAGE_URL}/original${movieDetailsData.posterPath}",
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                        )
                                    }

                                    movieDetailsData.id?.let { id ->
                                        val isInWatchlist by
                                            movieDetailsViewModel.isMovieInWatchlist
                                                .collectAsState()

                                        FilledIconToggleButton(
                                            checked = isInWatchlist,
                                            onCheckedChange = {
                                                if (isInWatchlist)
                                                    movieDetailsViewModel.removeMovieFromWatchlist()
                                                else movieDetailsViewModel.addMovieToWatchlist()
                                            },
                                        ) {
                                            Icon(
                                                painter =
                                                    painterResource(
                                                        if (!isInWatchlist)
                                                            R.drawable.ic_eye_slash_bold
                                                        else R.drawable.ic_eye_fill
                                                    ),
                                                contentDescription = null,
                                            )
                                        }
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        movieDetailsData.status?.let { status ->
                                            AttributeSurface(
                                                iconResId = R.drawable.ic_strategy_fill,
                                                value = status,
                                            )
                                        }

                                        StringFormatter.formatAsCurrency(
                                                value = movieDetailsData.revenue
                                            )
                                            ?.let { revenue ->
                                                AttributeSurface(
                                                    iconResId = R.drawable.ic_money_fill,
                                                    value = revenue,
                                                )
                                            }
                                    }

                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        val releaseDate =
                                            StringFormatter.formatReleaseDate(
                                                movieDetailsData.releaseDate
                                            )

                                        val metadata =
                                            movieDetailsData.genres
                                                .map { it.name }
                                                .joinToString(
                                                    separator = " • ",
                                                    prefix = "$releaseDate • ",
                                                )

                                        Text(
                                            text = metadata,
                                            modifier = Modifier.fillMaxWidth().alpha(0.6f),
                                            color = colorScheme.onSurface,
                                            textAlign = TextAlign.Center,
                                            style = typography.bodyMedium,
                                        )

                                        movieDetailsData.overview?.let { overview ->
                                            ExpandableText(
                                                text = overview,
                                                modifier =
                                                    Modifier.padding(start = 16.dp, end = 16.dp)
                                                        .fillMaxWidth()
                                                        .alpha(alpha = 0.87f),
                                                color = colorScheme.onSurface,
                                                textAlign = TextAlign.Center,
                                                maxLinesBeforeExpansion = 3,
                                                style = typography.bodyLarge,
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            LaunchedEffect(Unit) { movieDetailsViewModel.fetchSimilarMovies() }

                            val similarMoviesUiState by
                                movieDetailsViewModel.similarMoviesUiState.collectAsState()

                            Crossfade(
                                targetState = similarMoviesUiState,
                                label = "similarMoviesUiStateCrossfade",
                            ) { uiState ->
                                when (uiState) {
                                    is UiState.Error -> {
                                        Toast.makeText(
                                                LocalContext.current,
                                                uiState.message,
                                                Toast.LENGTH_SHORT,
                                            )
                                            .show()
                                    }

                                    UiState.Loading -> {
                                        Box(
                                            modifier = Modifier.fillMaxSize().padding(all = 16.dp),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.width(48.dp)
                                            )
                                        }
                                    }

                                    is UiState.Success -> {
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = stringResource(R.string.similar_to_this),
                                                modifier =
                                                    Modifier.fillMaxWidth()
                                                        .alpha(alpha = 0.87f)
                                                        .padding(
                                                            start = 16.dp,
                                                            top = 16.dp,
                                                            end = 16.dp,
                                                        ),
                                                color = colorScheme.onSurface,
                                                textAlign = TextAlign.Center,
                                                style = typography.titleLarge,
                                            )

                                            val similarMoviesData = uiState.data

                                            LazyRow(
                                                modifier = Modifier.fillMaxWidth(),
                                                state = rememberLazyListState(),
                                                contentPadding =
                                                    PaddingValues(
                                                        start = 16.dp,
                                                        top = 16.dp,
                                                        end = 16.dp,
                                                    ),
                                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                items(
                                                    items = similarMoviesData.results.take(5),
                                                    key = {
                                                        it.id
                                                            ?: "item_at_${similarMoviesData.results.indexOf(it)}"
                                                    },
                                                ) { movie ->
                                                    SimilarMovieCard(
                                                        navHostController = navHostController,
                                                        movieItemData = movie,
                                                    )
                                                }
                                            }

                                            LaunchedEffect(Unit) {
                                                movieDetailsViewModel.fetchSimilarMoviesAndCast(
                                                    similarMoviesData
                                                )
                                            }

                                            val movieCreditsUiState by
                                                movieDetailsViewModel.movieCreditsUiState
                                                    .collectAsState()

                                            Crossfade(
                                                targetState = movieCreditsUiState,
                                                label = "movieCreditsUiStateCrossFade",
                                            ) { uiState ->
                                                when (uiState) {
                                                    is UiState.Error -> {
                                                        Toast.makeText(
                                                                LocalContext.current,
                                                                uiState.message,
                                                                Toast.LENGTH_SHORT,
                                                            )
                                                            .show()
                                                    }

                                                    UiState.Loading -> {
                                                        Box(
                                                            modifier =
                                                                Modifier.fillMaxSize()
                                                                    .padding(all = 16.dp),
                                                            contentAlignment = Alignment.Center,
                                                        ) {
                                                            CircularProgressIndicator(
                                                                modifier = Modifier.width(48.dp)
                                                            )
                                                        }
                                                    }

                                                    is UiState.Success -> {
                                                        val (actorsData, directorsData) =
                                                            uiState.data

                                                        Column(modifier = Modifier.fillMaxWidth()) {
                                                            Text(
                                                                text =
                                                                    stringResource(
                                                                        R.string.associated_actors
                                                                    ),
                                                                modifier =
                                                                    Modifier.fillMaxWidth()
                                                                        .alpha(alpha = 0.87f)
                                                                        .padding(
                                                                            start = 16.dp,
                                                                            top = 16.dp,
                                                                            end = 16.dp,
                                                                        ),
                                                                color = colorScheme.onSurface,
                                                                textAlign = TextAlign.Center,
                                                                style = typography.titleLarge,
                                                            )

                                                            LazyRow(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                state = rememberLazyListState(),
                                                                contentPadding =
                                                                    PaddingValues(
                                                                        start = 16.dp,
                                                                        top = 16.dp,
                                                                        end = 16.dp,
                                                                    ),
                                                                horizontalArrangement =
                                                                    Arrangement.spacedBy(16.dp),
                                                                verticalAlignment =
                                                                    Alignment.CenterVertically,
                                                            ) {
                                                                items(
                                                                    items = actorsData,
                                                                    key = {
                                                                        it.id
                                                                            ?: "item_at_${actorsData.indexOf(it)}"
                                                                    },
                                                                ) { person ->
                                                                    PersonCard(
                                                                        person =
                                                                            Person.Actor(person)
                                                                    )
                                                                }
                                                            }

                                                            Text(
                                                                text =
                                                                    stringResource(
                                                                        R.string
                                                                            .associated_directors
                                                                    ),
                                                                modifier =
                                                                    Modifier.fillMaxWidth()
                                                                        .alpha(alpha = 0.87f)
                                                                        .padding(
                                                                            start = 16.dp,
                                                                            top = 16.dp,
                                                                            end = 16.dp,
                                                                        ),
                                                                color = colorScheme.onSurface,
                                                                textAlign = TextAlign.Center,
                                                                style = typography.titleLarge,
                                                            )

                                                            LazyRow(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                state = rememberLazyListState(),
                                                                contentPadding =
                                                                    PaddingValues(
                                                                        start = 16.dp,
                                                                        top = 16.dp,
                                                                        end = 16.dp,
                                                                    ),
                                                                horizontalArrangement =
                                                                    Arrangement.spacedBy(16.dp),
                                                                verticalAlignment =
                                                                    Alignment.CenterVertically,
                                                            ) {
                                                                items(
                                                                    items = directorsData,
                                                                    key = {
                                                                        it.id
                                                                            ?: "item_at_${directorsData.indexOf(it)}"
                                                                    },
                                                                ) { person ->
                                                                    PersonCard(
                                                                        person =
                                                                            Person.Director(person)
                                                                    )
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
                        }
                    }
                }
            }
        }
    }
}
