@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import xyz.kewiany.menusy.test.common.BaseTest
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository

class FetchSpokenLanguagesUseCaseTest : BaseTest() {

    @Test
    fun success_returnsLanguages() = runTest {
        val repo = mockk<MovieRepository> { coEvery { fetchSpokenLanguages(1) } returns listOf("English", "Polish") }
        val useCase = FetchSpokenLanguagesUseCase(repo)
        val result = useCase(1)
        assertTrue(result is Result.Success)
        assertEquals(listOf("English", "Polish"), (result as Result.Success).data)
    }

    @Test
    fun error_returnsResultError() = runTest {
        val repo = mockk<MovieRepository> { coEvery { fetchSpokenLanguages(2) } throws RuntimeException("boom") }
        val useCase = FetchSpokenLanguagesUseCase(repo)
        val result = useCase(2)
        assertTrue(result is Result.Error)
    }
}


