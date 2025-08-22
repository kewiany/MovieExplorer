package xyz.kewiany.movieexplorer.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import xyz.kewiany.movieexplorer.common.utils.MOVIE_ID
import xyz.kewiany.movieexplorer.presentation.feature.details.MovieDetailsScreen
import xyz.kewiany.movieexplorer.presentation.feature.details.MovieDetailsViewModel
import xyz.kewiany.movieexplorer.presentation.feature.favorites.FavoritesScreen
import xyz.kewiany.movieexplorer.presentation.feature.favorites.FavoritesViewModel
import xyz.kewiany.movieexplorer.presentation.feature.languages.MovieLanguagesScreen
import xyz.kewiany.movieexplorer.presentation.feature.languages.MovieLanguagesViewModel
import xyz.kewiany.movieexplorer.presentation.feature.popular.PopularMoviesScreen
import xyz.kewiany.movieexplorer.presentation.feature.popular.PopularMoviesViewModel
import xyz.kewiany.movieexplorer.presentation.feature.search.SearchScreen
import xyz.kewiany.movieexplorer.presentation.feature.search.SearchViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = NavigationDirections.popularMovies.destination
        ) {
            PopularMoviesDestination()
        }

        composable(
            route = NavigationDirections.search.destination
        ) {
            SearchDestination()
        }

        composable(
            route = NavigationDirections.favorites.destination
        ) {
            FavoritesDestination()
        }

        composable(
            route = "movie_details/{$MOVIE_ID}",
            arguments = NavigationDirections.movieDetails(0).arguments
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt(MOVIE_ID) ?: 0
            MovieDetailsDestination(movieId = movieId)
        }

        dialog(
            route = "movie_languages/{$MOVIE_ID}",
            arguments = NavigationDirections.movieLanguages(0).arguments
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt(MOVIE_ID) ?: 0
            MovieLanguagesDestination(movieId)
        }
    }
}

@Composable
private fun PopularMoviesDestination() {
    val viewModel: PopularMoviesViewModel = hiltViewModel()
    PopularMoviesScreen(
        state = viewModel.state.collectAsState(),
        eventHandler = viewModel::handleEvent,
    )
}

@Composable
private fun SearchDestination() {
    val viewModel: SearchViewModel = hiltViewModel()
    SearchScreen(
        state = viewModel.state.collectAsState(),
        eventHandler = viewModel::handleEvent
    )
}

@Composable
private fun MovieDetailsDestination(movieId: Int) {
    val viewModel: MovieDetailsViewModel = hiltViewModel()
    MovieDetailsScreen(
        movieId = movieId,
        state = viewModel.state.collectAsState(),
        eventHandler = viewModel::handleEvent
    )
}

@Composable
private fun FavoritesDestination() {
    val viewModel: FavoritesViewModel = hiltViewModel()
    FavoritesScreen(
        state = viewModel.state.collectAsState(),
        eventHandler = viewModel::handleEvent
    )
}

@Composable
private fun MovieLanguagesDestination(movieId: Int) {
    val viewModel: MovieLanguagesViewModel = hiltViewModel()
    MovieLanguagesScreen(
        movieId = movieId,
        state = viewModel.state.collectAsState(),
        eventHandler = viewModel::handleEvent
    )
}