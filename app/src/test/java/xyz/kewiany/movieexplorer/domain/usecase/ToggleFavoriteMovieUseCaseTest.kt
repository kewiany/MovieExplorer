@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.domain.usecase

import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository

class ToggleFavoriteMovieUseCaseTest {

    @Test
    fun when_invoke_then_delegatesToRepository() = runTest {
        val favoriteRepository = mockk<FavoriteMovieRepository> {
            coJustRun { toggleFavorite(5) }
        }
        val useCase = ToggleFavoriteMovieUseCase(favoriteRepository)
        useCase(5)
        runCurrent()
        coVerify { favoriteRepository.toggleFavorite(5) }
    }
}


