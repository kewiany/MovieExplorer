package xyz.kewiany.movieexplorer.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.domain.model.Movie
import xyz.kewiany.movieexplorer.domain.model.MoviesPage
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository

class SearchMoviesUseCaseTest {

    @Test
    fun `maps search results to MovieUi and overlays favorites`() = runTest {
        val movieRepository = mockk<MovieRepository> {
            coEvery { searchMovies("query", 1) } returns MoviesPage(
                movies = listOf(
                    Movie(1, "One", null, ""),
                    Movie(2, "Two", null, ""),
                ),
                currentPage = 1,
                totalPages = 1,
                totalResults = 2
            )
        }
        val favoriteRepository = mockk<FavoriteMovieRepository> {
            coEvery { observeFavorites() } returns MutableStateFlow(setOf(2))
        }

        val useCase = SearchMoviesUseCase(movieRepository, favoriteRepository)
        val result = useCase("query")

        assertTrue(result is Result.Success)
        val list = (result as Result.Success).data
        assertEquals(2, list.size)
        assertEquals(false, list[0].isFavorite)
        assertEquals(true, list[1].isFavorite)
    }
}


