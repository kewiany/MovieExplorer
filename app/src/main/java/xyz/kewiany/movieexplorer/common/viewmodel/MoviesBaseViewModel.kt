package xyz.kewiany.movieexplorer.common.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.kewiany.movieexplorer.common.navigation.NavigationDirections
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.domain.usecase.ToggleFavoriteMovieUseCase

abstract class MoviesBaseViewModel<S, E>(
    initialState: S,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase,
    private val navigator: Navigator,
) : BaseViewModel<S, E>(initialState) {

    protected fun navigateToMovieDetails(movieId: Int) {
        viewModelScope.launch { navigator.navigate(NavigationDirections.movieDetails(movieId)) }
    }

    protected fun toggleFavoriteMovie(movieId: Int) {
        viewModelScope.launch { toggleFavoriteMovieUseCase(movieId) }
    }
}


