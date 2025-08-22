@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.domain.usecase

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.createMovieDetails
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository

class GetMovieDetailsUseCaseTest {

    @Test
    fun when_invoke_then_returnsSuccess() = runTest {
        val movieRepository = mockk<MovieRepository> {
            coJustRun { fetchMovieDetails(1) }
            coEvery { observeMovieDetails(1) } returns flow {
                emit(createMovieDetails(1))
            }
        }
        val useCase = GetMovieDetailsUseCase(movieRepository)
        val result = useCase(1)
        assert(result is Result.Success)
        assertEquals(1, (result as Result.Success).data.id)
    }
}


