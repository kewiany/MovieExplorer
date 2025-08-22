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

class FetchOverviewUseCaseTest : BaseTest() {

    @Test
    fun success_returnsResultSuccess() = runTest {
        val repo = mockk<MovieRepository> { coEvery { fetchOverview(1) } returns "ov" }
        val useCase = FetchOverviewUseCase(repo)
        val result = useCase(1)
        assertTrue(result is Result.Success)
        assertEquals("ov", (result as Result.Success).data)
    }

    @Test
    fun error_returnsResultError() = runTest {
        val repo = mockk<MovieRepository> { coEvery { fetchOverview(2) } throws RuntimeException("boom") }
        val useCase = FetchOverviewUseCase(repo)
        val result = useCase(2)
        assertTrue(result is Result.Error)
    }
}


