package xyz.kewiany.movieexplorer.domain.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import xyz.kewiany.movieexplorer.domain.model.Movie
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository
import xyz.kewiany.movieexplorer.presentation.MovieUi
import javax.inject.Inject

class GetMoviesPagedUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteMovieRepository,
) {
    fun pagedPopularWithFavorites(scope: CoroutineScope): Flow<PagingData<MovieUi>> {
        val cached = movieRepository.pagerPopularMovies().cachedIn(scope)
        return combine(favoriteRepository.observeFavorites(), cached) { favIds, paging ->
            paging.map { movie -> movie.toMovieUi(favIds) }
        }
    }

    fun favoritesList() =
        combine(favoriteRepository.observeFavorites(), movieRepository.observeAllMovies()) { favIds, movies ->
            movies.filter { favIds.contains(it.id) }.map { it.toMovieUi(favIds) }
        }
}

private fun Movie.toMovieUi(favIds: Set<Int>) = MovieUi(
    id = id,
    title = title,
    posterPath = posterPath,
    isFavorite = favIds.contains(id),
)
