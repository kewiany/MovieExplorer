@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.presentation.feature.popular

import androidx.paging.PagingData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.menusy.test.common.BaseTest
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.domain.usecase.GetMoviesPagedUseCase
import xyz.kewiany.movieexplorer.domain.usecase.ToggleFavoriteMovieUseCase
import xyz.kewiany.movieexplorer.presentation.MovieUi

class PopularMoviesViewModelTest : BaseTest() {

    @Test
    fun when_movieClicked_then_emitsNavigationToDetails() = runTest {
        val navigator = Navigator()
        val get = mockk<GetMoviesPagedUseCase> {
            coEvery { pagedPopularWithFavorites(any()) } returns MutableStateFlow(PagingData.from(emptyList()))
        }
        val toggle =
            mockk<ToggleFavoriteMovieUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Success(Unit) }
        val viewModel = PopularMoviesViewModel(get, toggle, navigator)

        var dest: String? = null
        val job = launch { navigator.commands.collect { dest = it.destination } }
        viewModel.eventHandler(PopularMoviesViewModel.Event.MovieClicked(42))
        runCurrent()
        job.cancel()
        assertEquals("movie_details/42", dest)
    }

    @Test
    fun when_toggleFavorite_then_usecaseInvoked() = runTest {
        val navigator = Navigator()
        val get = mockk<GetMoviesPagedUseCase> {
            coEvery { pagedPopularWithFavorites(any()) } returns MutableStateFlow(PagingData.from(emptyList<MovieUi>()))
        }
        val toggle = mockk<ToggleFavoriteMovieUseCase> {
            coEvery { this@mockk.invoke(5) } returns Result.Success(Unit)
        }
        val viewModel = PopularMoviesViewModel(get, toggle, navigator)

        viewModel.eventHandler(PopularMoviesViewModel.Event.ToggleFavorite(5))
        runCurrent()
        io.mockk.coVerify(exactly = 1) { toggle.invoke(5) }
    }

    @Test
    fun when_searchClicked_then_navigatesToSearch() = runTest {
        val navigator = Navigator()
        val get = mockk<GetMoviesPagedUseCase> {
            coEvery { pagedPopularWithFavorites(any()) } returns MutableStateFlow(PagingData.from(emptyList<MovieUi>()))
        }
        val toggle =
            mockk<ToggleFavoriteMovieUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Success(Unit) }
        val viewModel = PopularMoviesViewModel(get, toggle, navigator)

        var dest: String? = null
        val job = launch { navigator.commands.collect { dest = it.destination } }
        viewModel.eventHandler(PopularMoviesViewModel.Event.SearchClicked)
        runCurrent()
        job.cancel()
        assertEquals("search", dest)
    }

    @Test
    fun toggleFavorite_delegatesToUseCase() = runTest {
        val navigator = Navigator()
        val get = mockk<GetMoviesPagedUseCase> {
            coEvery { pagedPopularWithFavorites(any()) } returns MutableStateFlow(PagingData.from(emptyList<MovieUi>()))
        }
        val toggle = mockk<ToggleFavoriteMovieUseCase> {
            coEvery { this@mockk.invoke(5) } returns Result.Success(Unit)
        }
        val vm = PopularMoviesViewModel(get, toggle, navigator)

        vm.eventHandler(PopularMoviesViewModel.Event.ToggleFavorite(5))
        runCurrent()
        io.mockk.coVerify(exactly = 1) { toggle.invoke(5) }
    }

    @Test
    fun init_exposesCachedPager_andNotLoading() = runTest {
        val navigator = Navigator()
        val get = mockk<GetMoviesPagedUseCase> {
            coEvery { pagedPopularWithFavorites(any()) } returns MutableStateFlow(PagingData.from(emptyList<MovieUi>()))
        }
        val toggle =
            mockk<ToggleFavoriteMovieUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Success(Unit) }
        val vm = PopularMoviesViewModel(get, toggle, navigator)

        runCurrent()
        val state = vm.state.value
        assert(state.moviesPaged != null)
        assertEquals(false, state.isLoading)
    }
}


