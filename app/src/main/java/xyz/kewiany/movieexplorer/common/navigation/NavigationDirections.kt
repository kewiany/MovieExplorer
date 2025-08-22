package xyz.kewiany.movieexplorer.common.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object NavigationDirections {

    val popularMovies = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "popular_movies"
    }

    val search = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "search"
    }

    val favorites = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val destination = "favorites"
    }

    fun movieDetails(movieId: Int) = object : NavigationCommand {
        override val arguments = listOf(
            navArgument("movieId") { type = NavType.IntType }
        )
        override val destination = "movie_details/$movieId"
    }

    fun movieLanguages(movieId: Int) = object : NavigationCommand {
        override val arguments = listOf(
            navArgument("movieId") { type = NavType.IntType }
        )
        override val destination = "movie_languages/$movieId"
    }
}
