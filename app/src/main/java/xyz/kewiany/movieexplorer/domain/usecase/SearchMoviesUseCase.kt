package xyz.kewiany.movieexplorer.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.domain.model.Movie
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository
import xyz.kewiany.movieexplorer.presentation.MovieUi
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SearchMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoritesRepository: FavoriteMovieRepository,
) {
    suspend operator fun invoke(query: String): Result<List<MovieUi>> = withContext(Dispatchers.IO) {
        try {
            val page = movieRepository.searchMovies(query = query, page = 1)
            val favIds = favoritesRepository.observeFavorites().first()
            val ui = page.movies.map { it.toMovieUi(favIds) }
            Result.Success(ui)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(e)
        }
    }
}

private fun Movie.toMovieUi(favIds: Set<Int>) = MovieUi(
    id = id,
    title = title,
    posterPath = posterPath,
    isFavorite = favIds.contains(id),
)


