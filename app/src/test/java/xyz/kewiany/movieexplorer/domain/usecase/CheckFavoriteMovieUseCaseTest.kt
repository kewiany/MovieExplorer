@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository

class CheckFavoriteMovieUseCaseTest {

    @Test
    fun when_repoReturnsTrue_then_successTrue() = runTest {
        val favoriteMovieRepository = mockk<FavoriteMovieRepository> { coEvery { isFavorite(1) } returns true }
        val useCase = CheckFavoriteMovieUseCase(favoriteMovieRepository)
        val result = useCase(1)
        assert(result is Result.Success)
        assertEquals(true, (result as Result.Success).data)
    }

    @Test
    fun when_repoThrows_then_error() = runTest {
        val favoriteMovieRepository =
            mockk<FavoriteMovieRepository> { coEvery { isFavorite(2) } throws IllegalStateException("db") }
        val useCase = CheckFavoriteMovieUseCase(favoriteMovieRepository)
        val result = useCase(2)
        assert(result is Result.Error)
    }
}