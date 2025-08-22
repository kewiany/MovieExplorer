@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.data.repositorysitory

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import xyz.kewiany.movieexplorer.createMovieDetailsResponse
import xyz.kewiany.movieexplorer.createMovieEntity
import xyz.kewiany.movieexplorer.createMovieItemResponse
import xyz.kewiany.movieexplorer.data.api.TMDBApi
import xyz.kewiany.movieexplorer.data.dao.MovieDao
import xyz.kewiany.movieexplorer.data.repository.MovieRepositoryImpl
import xyz.kewiany.movieexplorer.data.response.SearchMoviesResponse

class MovieRepositoryImplTest {

    @Test
    fun when_searchMovies_then_mapsToPage() = runTest {
        val api = mockk<TMDBApi> {
            coEvery { searchMovies("q", 1) } returns SearchMoviesResponse(
                page = 1,
                results = listOf(createMovieItemResponse(1), createMovieItemResponse(2)),
                totalPages = 1,
                totalResults = 2,
            )
        }
        val dao = mockk<MovieDao>(relaxed = true)
        val repository = MovieRepositoryImpl(dao, api)

        val page = repository.searchMovies("q", 1)
        assertEquals(2, page.movies.size)
        assertEquals(1, page.currentPage)
    }

    @Test
    fun when_fetchMovieDetails_then_upsertEntity() = runTest {
        val api = mockk<TMDBApi> {
            coEvery { getMovieDetails(7) } returns createMovieDetailsResponse(7)
        }
        val dao = mockk<MovieDao> {
            coJustRun { upsert(any()) }
        }
        val repository = MovieRepositoryImpl(dao, api)

        repository.fetchMovieDetails(7)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun when_observeAllMovies_then_mapsEntitiesToDomain() = runTest {
        val entity = createMovieEntity(3)
        val dao = mockk<MovieDao> {
            coEvery { observeAll() } returns MutableStateFlow(listOf(entity))
        }
        val api = mockk<TMDBApi>(relaxed = true)
        val repository = MovieRepositoryImpl(dao, api)

        val list = repository.observeAllMovies().first()
        assertEquals(1, list.size)
        val movie = list.first()
        assertEquals(3, movie.id)
        assertEquals(entity.title, movie.title)
        assertEquals(entity.posterPath, movie.posterPath)
        assertEquals(entity.releaseDate ?: "", movie.releaseDate)
    }

    @Test
    fun when_searchMovies_apiThrows_then_propagates() = runTest {
        val api = mockk<TMDBApi> {
            coEvery { searchMovies("q", 1) } throws RuntimeException("boom")
        }
        val dao = mockk<MovieDao>(relaxed = true)
        val repository = MovieRepositoryImpl(dao, api)

        assertThrows(RuntimeException::class.java) {
            runTest { repository.searchMovies("q", 1) }
        }
    }

    @Test
    fun when_fetchMovieDetails_daoThrows_then_propagates() = runTest {
        val api = mockk<TMDBApi> {
            coEvery { getMovieDetails(9) } returns createMovieDetailsResponse(9)
        }
        val dao = mockk<MovieDao> {
            coEvery { upsert(any()) } throws IllegalStateException("db fail")
        }
        val repository = MovieRepositoryImpl(dao, api)

        assertThrows(IllegalStateException::class.java) {
            runTest { repository.fetchMovieDetails(9) }
        }
    }
}


