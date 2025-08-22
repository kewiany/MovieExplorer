package xyz.kewiany.movieexplorer.presentation.feature.favorites

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.common.viewmodel.MoviesBaseViewModel
import xyz.kewiany.movieexplorer.domain.usecase.GetMoviesPagedUseCase
import xyz.kewiany.movieexplorer.domain.usecase.ToggleFavoriteMovieUseCase
import xyz.kewiany.movieexplorer.presentation.MovieUi
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getMovies: GetMoviesPagedUseCase,
    toggleFavorite: ToggleFavoriteMovieUseCase,
    navigator: Navigator,
) : MoviesBaseViewModel<FavoritesViewModel.State, FavoritesViewModel.Event>(State(), toggleFavorite, navigator) {

    init {
        viewModelScope.launch {
            getMovies.favoritesList().collect { list ->
                updateState { it.copy(isLoading = false, movies = list) }
            }
        }
    }

    override fun handleEvent(event: Event) = when (event) {
        is Event.MovieClicked -> handleMovieClicked(event)
        is Event.ToggleFavorite -> handleToggleFavorite(event)
    }

    private fun handleMovieClicked(event: Event.MovieClicked) {
        navigateToMovieDetails(event.id)
    }

    private fun handleToggleFavorite(event: Event.ToggleFavorite) {
        toggleFavoriteMovie(event.id)
    }

    data class State(
        val isLoading: Boolean = true,
        val movies: List<MovieUi> = emptyList(),
    )

    sealed class Event {
        data class MovieClicked(val id: Int) : Event()
        data class ToggleFavorite(val id: Int) : Event()
    }
}


