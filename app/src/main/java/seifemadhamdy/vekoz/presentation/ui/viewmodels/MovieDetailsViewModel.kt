package seifemadhamdy.vekoz.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import seifemadhamdy.vekoz.domain.models.TmdbMoviesRepository
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel
@Inject
constructor(private val tmdbMoviesRepository: TmdbMoviesRepository) : ViewModel()
