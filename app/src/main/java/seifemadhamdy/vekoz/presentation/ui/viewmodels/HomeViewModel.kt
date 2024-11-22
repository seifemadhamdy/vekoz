package seifemadhamdy.vekoz.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import seifemadhamdy.vekoz.domain.models.TmdbMoviesRepository
import seifemadhamdy.vekoz.domain.models.TmdbSearchRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val tmdbMoviesRepository: TmdbMoviesRepository,
    private val tmdbSearchRepository: TmdbSearchRepository
) : ViewModel()
