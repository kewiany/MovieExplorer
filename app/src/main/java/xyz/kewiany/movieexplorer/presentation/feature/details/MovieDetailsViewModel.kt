package xyz.kewiany.movieexplorer.presentation.feature.details

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.common.navigation.NavigationDirections
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.common.viewmodel.BaseViewModel
import xyz.kewiany.movieexplorer.domain.model.MovieDetails
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository
import xyz.kewiany.movieexplorer.domain.usecase.CheckFavoriteMovieUseCase
import xyz.kewiany.movieexplorer.domain.usecase.FetchOverviewUseCase
import xyz.kewiany.movieexplorer.domain.usecase.FetchReleaseDateUseCase
import xyz.kewiany.movieexplorer.domain.usecase.ToggleFavoriteMovieUseCase
import xyz.kewiany.movieexplorer.presentation.feature.details.MovieDetailsViewModel.Event
import xyz.kewiany.movieexplorer.presentation.feature.details.MovieDetailsViewModel.State
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val fetchOverview: FetchOverviewUseCase,
    private val fetchReleaseDate: FetchReleaseDateUseCase,
    private val toggleFavoriteMovie: ToggleFavoriteMovieUseCase,
    private val checkFavoriteMovieUseCase: CheckFavoriteMovieUseCase,
    private val navigator: Navigator,
) : BaseViewModel<State, Event>(State()) {

    override fun handleEvent(event: Event) = when (event) {
        is Event.Load -> handleLoadMovie(event)
        is Event.ToggleFavorite -> handleToggleFavourite(event)
        is Event.ShowLanguages -> handleShowLanguages(event)
        Event.BackClicked -> handleBackClicked()
    }

    private fun handleLoadMovie(event: Event.Load) {
        val movieId = event.id
        viewModelScope.launch {
            updateState { it.copy(showLoading = true) }

            runCatching { movieRepository.fetchMovieDetails(movieId) }

            val base = runCatching { movieRepository.observeMovieDetails(movieId).first() }
                .getOrElse {
                    updateState { it.copy(showError = true, showLoading = false) }
                    return@launch
                }
            val isFav = runCatching {
                val fav = checkFavoriteMovieUseCase(movieId)
                (fav as? Result.Success)?.data ?: false
            }.getOrDefault(false)

            updateState { it.copy(movie = base, isFavorite = isFav, showError = false) }

            val overviewDeferred = async { fetchOverview(movieId) }
            val releaseDeferred = async { fetchReleaseDate(movieId) }

            val overview = (overviewDeferred.await() as? Result.Success)?.data
            val release = (releaseDeferred.await() as? Result.Success)?.data

            val updated = base.copy(
                overview = overview ?: base.overview,
                releaseDate = release ?: base.releaseDate,
            )
            updateState { it.copy(movie = updated, showLoading = false) }
        }
    }

    private fun handleToggleFavourite(event: Event.ToggleFavorite) {
        val movieId = event.id
        viewModelScope.launch {
            when (toggleFavoriteMovie(movieId)) {
                is Result.Success -> updateState { it.copy(isFavorite = !(it.isFavorite)) }
                is Result.Error -> updateState { it.copy(showError = true, showLoading = false) }
            }
        }
    }

    private fun handleBackClicked() {
        navigator.back()
    }

    private fun handleShowLanguages(event: Event.ShowLanguages) {
        viewModelScope.launch { navigator.navigate(NavigationDirections.movieLanguages(event.id)) }
    }

    data class State(
        val movie: MovieDetails? = null,
        val showError: Boolean = false,
        val showLoading: Boolean = false,
        val isFavorite: Boolean = false,
        val showLanguagesModal: Boolean = false,
        val languages: List<String> = emptyList(),
    )

    sealed class Event {
        data class Load(val id: Int) : Event()
        data class ToggleFavorite(val id: Int) : Event()
        data class ShowLanguages(val id: Int) : Event()
        data object BackClicked : Event()
    }
}