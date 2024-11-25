package seifemadhamdy.vekoz.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import seifemadhamdy.vekoz.R
import seifemadhamdy.vekoz.data.remote.dto.ResultsDto
import seifemadhamdy.vekoz.presentation.components.IllustrationBox
import seifemadhamdy.vekoz.presentation.components.MovieCard
import seifemadhamdy.vekoz.presentation.ui.viewmodels.HomeViewModel
import seifemadhamdy.vekoz.presentation.ui.viewmodels.HomeViewModel.Companion.UiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val popularMoviesUiState by homeViewModel.popularMovesUiState.collectAsState()

    Crossfade(targetState = popularMoviesUiState, label = "popularMoviesUiState") { uiState ->
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
                        Button(onClick = { homeViewModel.fetchPopularMovies() }) {
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
                val movieQuery by homeViewModel.movieQuery.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Column(
                            modifier =
                                Modifier.fillMaxWidth()
                                    .background(
                                        brush =
                                            Brush.verticalGradient(
                                                listOf(colorScheme.background, Color.Transparent)
                                            )
                                    )
                                    .padding(all = 16.dp)
                                    .animateContentSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            val isSearchBarVisible by
                                homeViewModel.isSearchBarVisible.collectAsState()

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
                                    modifier = Modifier.fillMaxWidth(fraction = 0.381966012f),
                                    alignment = Alignment.CenterStart,
                                    colorFilter = ColorFilter.tint(colorScheme.primary),
                                )

                                IconToggleButton(
                                    checked = isSearchBarVisible,
                                    onCheckedChange = { homeViewModel.toggleSearchBarVisibility() },
                                ) {
                                    Icon(
                                        painter =
                                            painterResource(
                                                if (!isSearchBarVisible)
                                                    R.drawable.ic_magnifying_glass_bold
                                                else R.drawable.ic_x_bold
                                            ),
                                        contentDescription = null,
                                        tint = colorScheme.onSurface,
                                    )
                                }
                            }

                            AnimatedVisibility(
                                visible = isSearchBarVisible,
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut(),
                            ) {
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
                                    shape = SearchBarDefaults.inputFieldShape,
                                    colors =
                                        OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor =
                                                SearchBarDefaults.inputFieldColors()
                                                    .focusedContainerColor,
                                            unfocusedContainerColor =
                                                SearchBarDefaults.inputFieldColors()
                                                    .unfocusedContainerColor,
                                            disabledContainerColor =
                                                SearchBarDefaults.inputFieldColors()
                                                    .disabledContainerColor,
                                            errorContainerColor =
                                                SearchBarDefaults.inputFieldColors()
                                                    .errorContainerColor,
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                            disabledBorderColor = Color.Transparent,
                                        ),
                                )
                            }
                        }
                    },
                ) { paddingValues ->
                    val layoutDirection = LocalLayoutDirection.current
                    val queriedMoviesUiState by homeViewModel.queriedMoviesUiState.collectAsState()

                    val movieData =
                        when {
                            movieQuery.isNullOrEmpty() -> uiState.data

                            queriedMoviesUiState is UiState.Success ->
                                (queriedMoviesUiState as UiState.Success<List<ResultsDto>>).data

                            else -> null
                        }.orEmpty()

                    Crossfade(targetState = movieData, label = "movieDataCrossfade") { data ->
                        if (data.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                state = rememberLazyListState(),
                                contentPadding =
                                    PaddingValues(
                                        start =
                                            paddingValues.calculateStartPadding(layoutDirection) +
                                                16.dp,
                                        top = paddingValues.calculateTopPadding() + 16.dp,
                                        end =
                                            paddingValues.calculateEndPadding(layoutDirection) +
                                                16.dp,
                                        bottom = paddingValues.calculateBottomPadding() + 16.dp,
                                    ),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                val groupedMoviesByReleaseYear =
                                    data
                                        .groupBy { it.releaseDate?.substringBefore("-") }
                                        .entries
                                        .sortedByDescending { it.key }

                                groupedMoviesByReleaseYear.forEach { (releaseYear, movies) ->
                                    item(key = "header_$releaseYear") {
                                        Text(
                                            text =
                                                (if (releaseYear?.isNotBlank() == true) releaseYear
                                                else stringResource(R.string.undefined)),
                                            modifier = Modifier.alpha(alpha = 0.6f),
                                            style = MaterialTheme.typography.headlineMedium,
                                        )
                                    }

                                    items(
                                        items = movies,
                                        key = { it.id ?: "item_at_${data.indexOf(it)}" },
                                    ) { movie ->
                                        MovieCard(
                                            movieItemData = movie,
                                            navHostController = navHostController,
                                            viewModel = homeViewModel,
                                        )
                                    }
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Crossfade(
                                    targetState = queriedMoviesUiState,
                                    label = "queriedMoviesUiStateCrossfade",
                                ) { uiState ->
                                    when (uiState) {
                                        is UiState.Error -> {
                                            IllustrationBox(
                                                modifier =
                                                    Modifier.fillMaxSize().padding(all = 16.dp),
                                                imageResId = R.drawable.ic_warning_fill,
                                                title = stringResource(R.string.plot_twist),
                                                body = uiState.message,
                                            )
                                        }
                                        UiState.Loading -> {
                                            Box(
                                                modifier =
                                                    Modifier.fillMaxSize().padding(all = 16.dp),
                                                contentAlignment = Alignment.Center,
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.width(48.dp)
                                                )
                                            }
                                        }

                                        is UiState.Success -> Unit
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
