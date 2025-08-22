@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.movieexplorer.domain.model.Movie
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository

class GetMoviesPagedUseCaseTest {

    @Test
    fun favoritesList_filtersByFavIds_andMapsToUi() = runTest {
        val favs = MutableStateFlow(setOf(2, 3))
        val favoriteRepository = mockk<FavoriteMovieRepository> {
            coEvery { observeFavorites() } returns favs
        }
        val movieRepository = mockk<MovieRepository> {
            coEvery { observeAllMovies() } returns MutableStateFlow(
                listOf(
                    Movie(1, "A", null, ""),
                    Movie(2, "B", null, ""),
                    Movie(3, "C", null, ""),
                )
            )
        }
        val useCase = GetMoviesPagedUseCase(movieRepository, favoriteRepository)

        val list = useCase.favoritesList().first()
        assertEquals(2, list.size)
        assertEquals(listOf(2, 3), list.map { it.id })
        assertEquals(true, list.all { it.isFavorite })
    }

    @Test
    fun favoritesList_emptyWhenNoFavorites() = runTest {
        val favoriteRepo = mockk<FavoriteMovieRepository> {
            coEvery { observeFavorites() } returns MutableStateFlow(emptySet())
        }
        val movieRepository = mockk<MovieRepository> {
            coEvery { observeAllMovies() } returns MutableStateFlow(
                listOf(
                    Movie(1, "A", "", ""),
                    Movie(2, "B", "", "")
                )
            )
        }
        val useCase = GetMoviesPagedUseCase(movieRepository, favoriteRepo)
        val list = useCase.favoritesList().first()
        assertEquals(0, list.size)
    }
}


