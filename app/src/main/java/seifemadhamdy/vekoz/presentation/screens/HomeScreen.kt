package seifemadhamdy.vekoz.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import seifemadhamdy.vekoz.R
import seifemadhamdy.vekoz.data.remote.dto.ResultsDto
import seifemadhamdy.vekoz.presentation.components.MovieCard
import seifemadhamdy.vekoz.presentation.ui.viewmodels.HomeViewModel
import seifemadhamdy.vekoz.presentation.ui.viewmodels.HomeViewModel.Companion.UiState
import kotlin.math.ln

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val popularMoviesUiState by homeViewModel.popularMovesUiState.collectAsState()

    when (popularMoviesUiState) {
        is UiState.Error -> {}
        UiState.Loading -> {}
        is UiState.Success -> {
            val hazeState = remember { HazeState() }
            val colorScheme = MaterialTheme.colorScheme
            val goldenRatioComplementary = 0.381966012f
            val movieQuery by homeViewModel.movieQuery.collectAsState()

            val hazeTint =
                HazeTint(
                    color =
                        colorScheme.primaryContainer.copy(
                            alpha = ((4.5f * ln(3.dp.value + 1)) + 2f) / 100f
                        )
                )

            val hazeStyle =
                HazeStyle(
                    backgroundColor = colorScheme.background,
                    tint = hazeTint,
                    blurRadius = 20.dp,
                    noiseFactor = 0.0625f,
                    fallbackTint = hazeTint,
                )

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    Column(
                        modifier =
                            Modifier.fillMaxWidth()
                                .hazeChild(state = hazeState, style = hazeStyle)
                                .padding(all = 16.dp)
                                .animateContentSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        val isSearchBarVisible by homeViewModel.isSearchBarVisible.collectAsState()

                        Row(
                            modifier =
                                Modifier.fillMaxWidth()
                                    .windowInsetsPadding(TopAppBarDefaults.windowInsets),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                model = R.drawable.logo_vekoz_typemark,
                                contentDescription = null,
                                modifier =
                                    Modifier.fillMaxWidth(fraction = goldenRatioComplementary),
                                alignment = Alignment.CenterStart,
                                colorFilter = ColorFilter.tint(colorScheme.primary),
                            )

                            IconToggleButton(
                                checked = isSearchBarVisible,
                                onCheckedChange = { homeViewModel.toggleSearchBarVisibility() },
                                colors =
                                    IconButtonDefaults.iconToggleButtonColors(
                                        contentColor = colorScheme.onPrimaryContainer,
                                        checkedContentColor = colorScheme.onPrimaryContainer,
                                    ),
                            ) {
                                Icon(
                                    painter =
                                        painterResource(
                                            if (!isSearchBarVisible)
                                                R.drawable.ic_magnifying_glass_bold
                                            else R.drawable.ic_x_bold
                                        ),
                                    contentDescription = null,
                                )
                            }
                        }

                        AnimatedVisibility(
                            visible = isSearchBarVisible,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut(),
                        ) {
                            val containerAlpha = 0.6f

                            OutlinedTextField(
                                value = movieQuery ?: "",
                                onValueChange = { value ->
                                    homeViewModel.updateMovieQuery(query = value)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        text =
                                            stringResource(
                                                R.string.find_a_movie_that_s_oscar_worthy
                                            ),
                                        modifier = Modifier.alpha(0.6f),
                                    )
                                },
                                singleLine = true,
                                shape = MaterialTheme.shapes.large,
                                colors =
                                    OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor =
                                            SearchBarDefaults.inputFieldColors()
                                                .focusedContainerColor
                                                .copy(alpha = containerAlpha),
                                        unfocusedContainerColor =
                                            SearchBarDefaults.inputFieldColors()
                                                .unfocusedContainerColor
                                                .copy(alpha = containerAlpha),
                                        disabledContainerColor =
                                            SearchBarDefaults.inputFieldColors()
                                                .disabledContainerColor
                                                .copy(alpha = containerAlpha),
                                        errorContainerColor =
                                            SearchBarDefaults.inputFieldColors()
                                                .errorContainerColor
                                                .copy(alpha = containerAlpha),
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        disabledBorderColor = Color.Transparent,
                                    ),
                            )
                        }
                    }
                },
                bottomBar = {
                    Box(
                        modifier =
                            Modifier.fillMaxWidth()
                                .height(
                                    height =
                                        WindowInsets.navigationBars
                                            .asPaddingValues()
                                            .calculateBottomPadding()
                                )
                                .hazeChild(state = hazeState, style = hazeStyle)
                    )
                },
            ) { paddingValues ->
                val layoutDirection = LocalLayoutDirection.current
                val lazyColumnPadding = 16.dp
                val queriedMoviesUiState by homeViewModel.queriedMoviesUiState.collectAsState()

                val movieData =
                    when {
                        movieQuery.isNullOrEmpty() ->
                            (popularMoviesUiState as UiState.Success<List<ResultsDto>>).data

                        queriedMoviesUiState is UiState.Success ->
                            (queriedMoviesUiState as UiState.Success<List<ResultsDto>>).data

                        else -> null
                    }.orEmpty()

                LazyColumn(
                    modifier = Modifier.fillMaxSize().haze(state = hazeState),
                    state = rememberLazyListState(),
                    contentPadding =
                        PaddingValues(
                            start =
                                paddingValues.calculateStartPadding(layoutDirection) +
                                    lazyColumnPadding,
                            top = paddingValues.calculateTopPadding() + lazyColumnPadding,
                            end =
                                paddingValues.calculateEndPadding(layoutDirection) +
                                    lazyColumnPadding,
                            bottom = paddingValues.calculateBottomPadding() + lazyColumnPadding,
                        ),
                    verticalArrangement = Arrangement.spacedBy(lazyColumnPadding),
                ) {
                    val groupedMoviesByReleaseYear =
                        movieData
                            .groupBy { it.releaseDate?.substringBefore("-") }
                            .entries
                            .sortedByDescending { it.key }

                    groupedMoviesByReleaseYear.forEach { (releaseYear, movies) ->
                        item(key = "header_$releaseYear") {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.extraLarge,
                                color = colorScheme.surfaceContainer,
                            ) {
                                Text(
                                    text =
                                        "${(if (releaseYear?.isNotBlank() == true) releaseYear
                                else "Unknown Year")} Releases",
                                    modifier =
                                        Modifier.fillMaxWidth()
                                            .padding(all = 16.dp)
                                            .alpha(alpha = 0.6f),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                        }

                        items(
                            items = movies,
                            key = { it.id ?: "item_at_${movieData.indexOf(it)}" },
                        ) { movie ->
                            MovieCard(
                                movieItemData = movie,
                                navHostController = navHostController,
                                viewModel = homeViewModel,
                            )
                        }
                    }
                }
            }
        }
    }
}
