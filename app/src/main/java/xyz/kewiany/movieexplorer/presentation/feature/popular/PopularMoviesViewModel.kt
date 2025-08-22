package xyz.kewiany.movieexplorer.presentation.feature.popular

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import xyz.kewiany.movieexplorer.common.navigation.NavigationDirections
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.common.viewmodel.MoviesBaseViewModel
import xyz.kewiany.movieexplorer.domain.usecase.GetMoviesPagedUseCase
import xyz.kewiany.movieexplorer.domain.usecase.ToggleFavoriteMovieUseCase
import xyz.kewiany.movieexplorer.presentation.MovieUi
import xyz.kewiany.movieexplorer.presentation.feature.popular.PopularMoviesViewModel.Event
import xyz.kewiany.movieexplorer.presentation.feature.popular.PopularMoviesViewModel.State
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    getMoviesPaged: GetMoviesPagedUseCase,
    toggleFavorite: ToggleFavoriteMovieUseCase,
    private val navigator: Navigator,
) : MoviesBaseViewModel<State, Event>(State(), toggleFavorite, navigator) {

    init {
        val paged: Flow<PagingData<MovieUi>> = getMoviesPaged.pagedPopularWithFavorites(viewModelScope)
        updateState { it.copy(moviesPaged = paged, isLoading = false) }
    }

    override fun handleEvent(event: Event) = when (event) {
        is Event.MovieClicked -> handleMovieClicked(event)
        Event.SearchClicked -> handleSearchClicked()
        is Event.ToggleFavorite -> handleToggleFavorite(event)
    }

    private fun handleMovieClicked(event: Event.MovieClicked) {
        navigateToMovieDetails(event.id)
    }

    private fun handleSearchClicked() {
        viewModelScope.launch { navigator.navigate(NavigationDirections.search) }
    }

    private fun handleToggleFavorite(event: Event.ToggleFavorite) {
        toggleFavoriteMovie(event.id)
    }

    data class State(
        val isLoading: Boolean = true,
        val movies: List<MovieUi> = emptyList(),
        val moviesPaged: Flow<PagingData<MovieUi>>? = null,
    )

    sealed class Event {
        data class MovieClicked(val id: Int) : Event()
        data object SearchClicked : Event()
        data class ToggleFavorite(val id: Int) : Event()
    }
}