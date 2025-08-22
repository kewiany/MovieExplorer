@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.presentation.feature.details

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.menusy.test.common.BaseTest
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.createMovieDetails
import xyz.kewiany.movieexplorer.domain.model.MovieDetails
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository
import xyz.kewiany.movieexplorer.domain.usecase.CheckFavoriteMovieUseCase
import xyz.kewiany.movieexplorer.domain.usecase.FetchOverviewUseCase
import xyz.kewiany.movieexplorer.domain.usecase.FetchReleaseDateUseCase
import xyz.kewiany.movieexplorer.domain.usecase.ToggleFavoriteMovieUseCase
import xyz.kewiany.movieexplorer.presentation.feature.details.MovieDetailsViewModel.Event

class MovieDetailsViewModelTest : BaseTest() {

    private fun viewModel(
        base: MovieDetails = createMovieDetails(1),
        overview: Result<String> = Result.Success(base.overview),
        release: Result<String> = Result.Success(base.releaseDate),
        isFav: Result<Boolean> = Result.Success(false),
    ): MovieDetailsViewModel {
        val repo = mockk<MovieRepository> {
            coJustRun { fetchMovieDetails(any()) }
            coEvery { observeMovieDetails(any()) } returns flow { emit(base) }
        }
        val fo = mockk<FetchOverviewUseCase> { coEvery { this@mockk.invoke(any()) } returns overview }
        val fr = mockk<FetchReleaseDateUseCase> { coEvery { this@mockk.invoke(any()) } returns release }
        val toggle = mockk<ToggleFavoriteMovieUseCase> { coJustRun { this@mockk.invoke(any()) } }
        val check = mockk<CheckFavoriteMovieUseCase> { coEvery { this@mockk.invoke(any()) } returns isFav }
        return MovieDetailsViewModel(repo, fo, fr, toggle, check, Navigator())
    }

    @Test
    fun when_ShowLanguages_then_navigatesToMovieLanguages() = runTest {
        val repo =
            mockk<MovieRepository> { coEvery { observeMovieDetails(any()) } returns flow { emit(createMovieDetails(1)) } }
        val fo = mockk<FetchOverviewUseCase>(relaxed = true)
        val fr = mockk<FetchReleaseDateUseCase>(relaxed = true)
        val toggle = mockk<ToggleFavoriteMovieUseCase>(relaxed = true)
        val check = mockk<CheckFavoriteMovieUseCase>(relaxed = true)
        val navigator = Navigator()
        val vm = MovieDetailsViewModel(repo, fo, fr, toggle, check, navigator)

        var dest: String? = null
        val job = launch { navigator.commands.collect { dest = it.destination } }
        vm.eventHandler(Event.ShowLanguages(99))
        runCurrent()
        job.cancel()

        assertEquals("movie_languages/99", dest)
    }

    @Test
    fun when_Load_success_then_stateHasMovie_andFavFlag() = runTest {
        val viewModel = viewModel()
        viewModel.eventHandler(Event.Load(1))
        runCurrent()

        assertEquals(false, viewModel.state.value.showLoading)
        assertEquals(1, viewModel.state.value.movie?.id)
        assertEquals(false, viewModel.state.value.isFavorite)
    }

    @Test
    fun when_Load_error_then_stateStopsLoading_andMovieNull() = runTest {
        val repo = mockk<MovieRepository> {
            coEvery { observeMovieDetails(any()) } returns flow {
                throw RuntimeException("boom")
            }
        }
        val fo = mockk<FetchOverviewUseCase>(relaxed = true)
        val fr = mockk<FetchReleaseDateUseCase>(relaxed = true)
        val toggle = mockk<ToggleFavoriteMovieUseCase>(relaxed = true)
        val check = mockk<CheckFavoriteMovieUseCase>(relaxed = true)
        val viewModel = MovieDetailsViewModel(repo, fo, fr, toggle, check, Navigator())
        viewModel.eventHandler(Event.Load(2))
        runCurrent()

        assertEquals(false, viewModel.state.value.showLoading)
        assertEquals(null, viewModel.state.value.movie)
    }

    @Test
    fun when_ToggleFavorite_success_then_flipFlag() = runTest {
        val repo =
            mockk<MovieRepository> { coEvery { observeMovieDetails(any()) } returns flow { emit(createMovieDetails(1)) } }
        val fo = mockk<FetchOverviewUseCase>(relaxed = true)
        val fr = mockk<FetchReleaseDateUseCase>(relaxed = true)
        val toggle =
            mockk<ToggleFavoriteMovieUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Success(Unit) }
        val check =
            mockk<CheckFavoriteMovieUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Success(false) }
        val viewModel = MovieDetailsViewModel(repo, fo, fr, toggle, check, Navigator())
        viewModel.eventHandler(Event.Load(1))
        runCurrent()
        viewModel.eventHandler(Event.ToggleFavorite(1))
        runCurrent()

        assertEquals(true, viewModel.state.value.isFavorite)
        coVerify { toggle.invoke(1) }
    }

    @Test
    fun when_ToggleFavorite_error_then_keepFlag() = runTest {
        val repo =
            mockk<MovieRepository> { coEvery { observeMovieDetails(any()) } returns flow { emit(createMovieDetails(1)) } }
        val fo = mockk<FetchOverviewUseCase>(relaxed = true)
        val fr = mockk<FetchReleaseDateUseCase>(relaxed = true)
        val toggle =
            mockk<ToggleFavoriteMovieUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Error(Exception("fail")) }
        val check =
            mockk<CheckFavoriteMovieUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Success(true) }
        val viewModel = MovieDetailsViewModel(repo, fo, fr, toggle, check, Navigator())
        viewModel.eventHandler(Event.Load(1))
        runCurrent()
        viewModel.eventHandler(Event.ToggleFavorite(1))
        runCurrent()

        assertEquals(true, viewModel.state.value.isFavorite)
    }

    @Test
    fun when_Load_setsLoadingTrue_thenFalse_onSuccess() = runTest {
        val repo = mockk<MovieRepository> {
            coEvery { observeMovieDetails(any()) } returns flow {
                kotlinx.coroutines.delay(1)
                emit(createMovieDetails(1))
            }
        }
        val fo = mockk<FetchOverviewUseCase>(relaxed = true)
        val fr = mockk<FetchReleaseDateUseCase>(relaxed = true)
        val toggle = mockk<ToggleFavoriteMovieUseCase>(relaxed = true)
        val check =
            mockk<CheckFavoriteMovieUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Success(false) }
        val viewModel = MovieDetailsViewModel(repo, fo, fr, toggle, check, Navigator())

        viewModel.eventHandler(Event.Load(1))
        runCurrent()
        assertEquals(true, viewModel.state.value.showLoading)
        advanceUntilIdle()
        assertEquals(false, viewModel.state.value.showLoading)
    }

    @Test
    fun when_Load_setsLoadingTrue_thenFalse_onError() = runTest {
        val repo = mockk<MovieRepository> {
            coJustRun { fetchMovieDetails(any()) }
            coEvery { observeMovieDetails(any()) } returns flow {
                kotlinx.coroutines.delay(1)
                throw RuntimeException("boom")
            }
        }
        val fo = mockk<FetchOverviewUseCase>(relaxed = true)
        val fr = mockk<FetchReleaseDateUseCase>(relaxed = true)
        val toggle = mockk<ToggleFavoriteMovieUseCase>(relaxed = true)
        val check = mockk<CheckFavoriteMovieUseCase>(relaxed = true)
        val viewModel = MovieDetailsViewModel(repo, fo, fr, toggle, check, Navigator())

        viewModel.eventHandler(Event.Load(2))
        runCurrent()
        assertEquals(true, viewModel.state.value.showLoading)
        advanceUntilIdle()
        assertEquals(false, viewModel.state.value.showLoading)
        assertEquals(true, viewModel.state.value.showError)
    }

    @Test
    fun when_BaseObserveFails_then_showErrorTrue_andNoMovie() = runTest {
        val repo = mockk<MovieRepository> {
            coJustRun { fetchMovieDetails(any()) }
            coEvery { observeMovieDetails(any()) } returns flow {
                throw RuntimeException("db-error")
            }
        }
        val fo = mockk<FetchOverviewUseCase>(relaxed = true)
        val fr = mockk<FetchReleaseDateUseCase>(relaxed = true)
        val toggle = mockk<ToggleFavoriteMovieUseCase>(relaxed = true)
        val check = mockk<CheckFavoriteMovieUseCase>(relaxed = true)
        val vm = MovieDetailsViewModel(repo, fo, fr, toggle, check, Navigator())

        vm.eventHandler(Event.Load(5))
        runCurrent()

        assertEquals(false, vm.state.value.showLoading)
        assertEquals(true, vm.state.value.showError)
        assertEquals(null, vm.state.value.movie)
    }

    @Test
    fun when_Load_then_Toggle_then_Load_again_flagRemainsConsistent() = runTest {
        val repo = mockk<MovieRepository> {
            coJustRun { fetchMovieDetails(any()) }
            coEvery { observeMovieDetails(any()) } returns flow { emit(createMovieDetails(1)) }
        }
        val fo = mockk<FetchOverviewUseCase>(relaxed = true)
        val fr = mockk<FetchReleaseDateUseCase>(relaxed = true)
        val toggle =
            mockk<ToggleFavoriteMovieUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Success(Unit) }
        val check = mockk<CheckFavoriteMovieUseCase> {
            coEvery { this@mockk.invoke(any()) } returnsMany listOf(Result.Success(false), Result.Success(true))
        }
        val viewModel = MovieDetailsViewModel(repo, fo, fr, toggle, check, Navigator())

        viewModel.eventHandler(Event.Load(1))
        runCurrent()
        assertEquals(false, viewModel.state.value.isFavorite)

        viewModel.eventHandler(Event.ToggleFavorite(1))
        runCurrent()
        assertEquals(true, viewModel.state.value.isFavorite)

        viewModel.eventHandler(Event.Load(1))
        runCurrent()
        assertEquals(true, viewModel.state.value.isFavorite)
        assertEquals(1, viewModel.state.value.movie?.id)
    }

    @Test
    fun when_overviewFails_releaseSucceeds_then_keepBaseOverview_updateRelease() = runTest {
        val base = createMovieDetails(10).copy(overview = "base", releaseDate = "base-date")
        val repo = mockk<MovieRepository> {
            coJustRun { fetchMovieDetails(any()) }
            coEvery { observeMovieDetails(any()) } returns flow { emit(base) }
        }
        val fo =
            mockk<FetchOverviewUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Error(Exception("fail")) }
        val fr =
            mockk<FetchReleaseDateUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Success("new-date") }
        val toggle = mockk<ToggleFavoriteMovieUseCase>(relaxed = true)
        val check =
            mockk<CheckFavoriteMovieUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Success(false) }
        val vm = MovieDetailsViewModel(repo, fo, fr, toggle, check, Navigator())

        vm.eventHandler(Event.Load(10))
        advanceUntilIdle()

        val movie = vm.state.value.movie!!
        assertEquals("base", movie.overview)
        assertEquals("new-date", movie.releaseDate)
    }

    @Test
    fun when_bothConcurrentFail_then_baseRemains() = runTest {
        val base = createMovieDetails(11).copy(overview = "B", releaseDate = "R")
        val repo = mockk<MovieRepository> {
            coJustRun { fetchMovieDetails(any()) }
            coEvery { observeMovieDetails(any()) } returns flow { emit(base) }
        }
        val fo =
            mockk<FetchOverviewUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Error(Exception("fail")) }
        val fr =
            mockk<FetchReleaseDateUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Error(Exception("fail")) }
        val toggle = mockk<ToggleFavoriteMovieUseCase>(relaxed = true)
        val check = mockk<CheckFavoriteMovieUseCase>(relaxed = true)
        val vm = MovieDetailsViewModel(repo, fo, fr, toggle, check, Navigator())

        vm.eventHandler(Event.Load(11))
        advanceUntilIdle()

        val movie = vm.state.value.movie!!
        assertEquals("B", movie.overview)
        assertEquals("R", movie.releaseDate)
    }

    @Test
    fun when_overviewSucceeds_releaseFails_then_updateOverview_keepBaseRelease() = runTest {
        val base = createMovieDetails(12).copy(overview = "old", releaseDate = "base-r")
        val repo = mockk<MovieRepository> {
            coJustRun { fetchMovieDetails(any()) }
            coEvery { observeMovieDetails(any()) } returns flow { emit(base) }
        }
        val fo = mockk<FetchOverviewUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Success("new-ov") }
        val fr =
            mockk<FetchReleaseDateUseCase> { coEvery { this@mockk.invoke(any()) } returns Result.Error(Exception("fail")) }
        val toggle = mockk<ToggleFavoriteMovieUseCase>(relaxed = true)
        val check = mockk<CheckFavoriteMovieUseCase>(relaxed = true)
        val vm = MovieDetailsViewModel(repo, fo, fr, toggle, check, Navigator())

        vm.eventHandler(Event.Load(12))
        advanceUntilIdle()

        val movie = vm.state.value.movie!!
        assertEquals("new-ov", movie.overview)
        assertEquals("base-r", movie.releaseDate)
    }
}


