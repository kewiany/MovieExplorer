@file:OptIn(ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.presentation.feature.favorites

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.menusy.test.common.BaseTest
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.createMovie
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import xyz.kewiany.movieexplorer.domain.usecase.GetMoviesPagedUseCase
import xyz.kewiany.movieexplorer.domain.usecase.ToggleFavoriteMovieUseCase
import xyz.kewiany.movieexplorer.presentation.feature.favorites.FavoritesViewModel.Event

class FavoritesViewModelTest : BaseTest() {

    @Test
    fun when_init_then_collectFavoritesIntoState() = runTest {
        val movies = listOf(createMovie(1))
        val getMovies = mockk<GetMoviesPagedUseCase> {
            coEvery { favoritesList() } returns MutableStateFlow(movies)
        }
        val favoriteRepo = mockk<FavoriteMovieRepository>(relaxed = true) {
            coEvery { observeFavorites() } returns MutableStateFlow(setOf(1))
        }
        val viewModel = FavoritesViewModel(getMovies, ToggleFavoriteMovieUseCase(favoriteRepo), Navigator())

        runCurrent()

        assertEquals(false, viewModel.state.value.isLoading)
        assertEquals(1, viewModel.state.value.movies.size)
        assertEquals(1, viewModel.state.value.movies.first().id)
    }

    @Test
    fun when_toggleFavorite_event_then_repoCalled() = runTest {
        val movies = listOf(createMovie(10))
        val getMovies = mockk<GetMoviesPagedUseCase> {
            coEvery { favoritesList() } returns MutableStateFlow(movies)
        }
        val favoriteRepo = mockk<FavoriteMovieRepository>(relaxed = true) {
            coEvery { observeFavorites() } returns MutableStateFlow(emptySet())
            coJustRun { toggleFavorite(10) }
        }
        val viewModel = FavoritesViewModel(getMovies, ToggleFavoriteMovieUseCase(favoriteRepo), Navigator())

        runCurrent()
        viewModel.eventHandler(Event.ToggleFavorite(10))
        runCurrent()
        coVerify { favoriteRepo.toggleFavorite(10) }
    }

    @Test
    fun when_toggleFavorite_then_usecaseCalled() = runTest {
        val movies = listOf(createMovie(1))
        val getMovies = mockk<GetMoviesPagedUseCase> {
            coEvery { favoritesList() } returns MutableStateFlow(movies)
        }
        val favoriteRepo = mockk<FavoriteMovieRepository>(relaxed = true) {
            coEvery { observeFavorites() } returns MutableStateFlow(emptySet())
            coJustRun { toggleFavorite(1) }
        }
        val toggle = ToggleFavoriteMovieUseCase(favoriteRepo)
        val viewModel = FavoritesViewModel(getMovies, toggle, Navigator())

        runCurrent()
        viewModel.eventHandler(Event.ToggleFavorite(1))
        runCurrent()

        coVerify { favoriteRepo.toggleFavorite(1) }
    }

    @Test
    fun when_movieClicked_then_navigateToDetails() = runTest {
        val movies = listOf(createMovie(7))
        val getMovies = mockk<GetMoviesPagedUseCase> {
            coEvery { favoritesList() } returns MutableStateFlow(movies)
        }
        val favoriteRepo = mockk<FavoriteMovieRepository>(relaxed = true) {
            coEvery { observeFavorites() } returns MutableStateFlow(setOf(7))
        }
        val viewModel = FavoritesViewModel(getMovies, ToggleFavoriteMovieUseCase(favoriteRepo), Navigator())

        runCurrent()
        viewModel.eventHandler(Event.MovieClicked(7))
        runCurrent()

        assertEquals(7, viewModel.state.value.movies.first().id)
    }

    @Test
    fun when_favoritesFlowUpdates_then_stateMoviesUpdated() = runTest {
        val flow = MutableStateFlow(listOf(createMovie(1)))
        val getMovies = mockk<GetMoviesPagedUseCase> {
            coEvery { favoritesList() } returns flow
        }
        val favoriteRepo = mockk<FavoriteMovieRepository>(relaxed = true) {
            coEvery { observeFavorites() } returns MutableStateFlow(emptySet())
        }
        val viewModel = FavoritesViewModel(getMovies, ToggleFavoriteMovieUseCase(favoriteRepo), Navigator())

        runCurrent()
        assertEquals(1, viewModel.state.value.movies.size)

        flow.value = listOf(createMovie(1), createMovie(2))
        runCurrent()
        assertEquals(2, viewModel.state.value.movies.size)
        assertEquals(2, viewModel.state.value.movies.last().id)
    }

    @Test
    fun when_noFavorites_then_stateEmptyAndNotLoading() = runTest {
        val getMovies = mockk<GetMoviesPagedUseCase> {
            coEvery { favoritesList() } returns MutableStateFlow(emptyList())
        }
        val favoriteRepo = mockk<FavoriteMovieRepository>(relaxed = true) {
            coEvery { observeFavorites() } returns MutableStateFlow(emptySet())
        }
        val viewModel = FavoritesViewModel(getMovies, ToggleFavoriteMovieUseCase(favoriteRepo), Navigator())

        runCurrent()
        assertEquals(false, viewModel.state.value.isLoading)
        assertEquals(0, viewModel.state.value.movies.size)
    }

    @Test
    fun when_favoritesFlowRapidChanges_then_lastStateWins() = runTest {
        val flow = MutableStateFlow(listOf(createMovie(1)))
        val getMovies = mockk<GetMoviesPagedUseCase> {
            coEvery { favoritesList() } returns flow
        }
        val favoriteRepo = mockk<FavoriteMovieRepository>(relaxed = true) {
            coEvery { observeFavorites() } returns MutableStateFlow(emptySet())
        }
        val viewModel = FavoritesViewModel(getMovies, ToggleFavoriteMovieUseCase(favoriteRepo), Navigator())

        flow.value = listOf(createMovie(2))
        flow.value = listOf(createMovie(3))
        advanceUntilIdle()

        assertEquals(1, viewModel.state.value.movies.size)
        assertEquals(3, viewModel.state.value.movies.first().id)
    }

    @Test
    fun when_toggleFavoriteMultipleTimes_then_repositoryCalledNtimes() = runTest {
        val movies = listOf(createMovie(1))
        val getMovies = mockk<GetMoviesPagedUseCase> {
            coEvery { favoritesList() } returns MutableStateFlow(movies)
        }
        val favoriteRepo = mockk<FavoriteMovieRepository>(relaxed = true) {
            coEvery { observeFavorites() } returns MutableStateFlow(emptySet())
            coJustRun { toggleFavorite(any()) }
        }
        val viewModel = FavoritesViewModel(getMovies, ToggleFavoriteMovieUseCase(favoriteRepo), Navigator())

        runCurrent()
        viewModel.eventHandler(Event.ToggleFavorite(1))
        viewModel.eventHandler(Event.ToggleFavorite(1))
        advanceUntilIdle()

        coVerify(exactly = 2) { favoriteRepo.toggleFavorite(1) }
    }

    @Test
    fun when_movieClicked_then_navigateCommandEmitted() = runTest {
        val movies = listOf(createMovie(9))
        val getMovies = mockk<GetMoviesPagedUseCase> { coEvery { favoritesList() } returns MutableStateFlow(movies) }
        val favoriteRepo = mockk<FavoriteMovieRepository>(relaxed = true) {
            coEvery { observeFavorites() } returns MutableStateFlow(setOf(9))
        }
        val vm = FavoritesViewModel(getMovies, ToggleFavoriteMovieUseCase(favoriteRepo), Navigator())

        val job = launch { vm.state.collect { } }
        vm.eventHandler(Event.MovieClicked(9))
        runCurrent()
        job.cancel()
        assertEquals(9, vm.state.value.movies.first().id)
    }
}


