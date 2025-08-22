@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.presentation.feature.search

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.menusy.test.common.BaseTest
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.createMovie
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import xyz.kewiany.movieexplorer.domain.repository.SearchHistoryRepository
import xyz.kewiany.movieexplorer.domain.repository.SearchRepository
import xyz.kewiany.movieexplorer.domain.usecase.ClearSearchTextUseCase
import xyz.kewiany.movieexplorer.domain.usecase.GetSearchHistoryUseCase
import xyz.kewiany.movieexplorer.domain.usecase.GetSearchTextUseCase
import xyz.kewiany.movieexplorer.domain.usecase.SearchMoviesUseCase
import xyz.kewiany.movieexplorer.domain.usecase.ToggleFavoriteMovieUseCase

class SearchViewModelTest : BaseTest() {

    @Test
    fun debounce_rapidQueries_emitsSingleSearch() = runTest {
        val searchText = MutableStateFlow("")
        val getText = GetSearchTextUseCase(object : SearchRepository {
            override val searchText = searchText
            override fun setSearchText(text: String) {}
            override fun clearSearchText() {}
        })
        val search = mockk<SearchMoviesUseCase> {
            coEvery { this@mockk.invoke(any()) } returns Result.Success(emptyList())
        }
        val clearSearchText = mockk<ClearSearchTextUseCase>()
        val favoriteRepository =
            mockk<FavoriteMovieRepository> { coEvery { observeFavorites() } returns MutableStateFlow(emptySet()) }
        val toggle = ToggleFavoriteMovieUseCase(favoriteRepository)
        val historyRepo =
            mockk<SearchHistoryRepository> { coEvery { observeHistory() } returns MutableStateFlow(emptyList()) }
        val getHistory = GetSearchHistoryUseCase(historyRepo, favoriteRepository)
        val viewModel = SearchViewModel(
            toggle,
            search,
            getText,
            clearSearchText,
            favoriteRepository,
            historyRepo,
            getHistory,
            mockk(relaxed = true)
        )

        searchText.value = "a"; searchText.value = "ab"; searchText.value = "abc"
        delay(600)
        runCurrent()

        assertEquals("abc", viewModel.state.value.query)
    }

    @Test
    fun clearQuery_resetsFlagsAndResults() = runTest {
        val getText = GetSearchTextUseCase(object : SearchRepository {
            override val searchText = MutableStateFlow("")
            override fun setSearchText(text: String) {}
            override fun clearSearchText() {}
        })
        val search = mockk<SearchMoviesUseCase> {
            coEvery { this@mockk.invoke(any()) } returns Result.Success(listOf(createMovie(1)))
        }
        val clearSearchText = mockk<ClearSearchTextUseCase>()
        val favoriteRepository =
            mockk<FavoriteMovieRepository> { coEvery { observeFavorites() } returns MutableStateFlow(emptySet()) }
        val historyRepo =
            mockk<SearchHistoryRepository> { coEvery { observeHistory() } returns MutableStateFlow(emptyList()) }
        val getHistory = GetSearchHistoryUseCase(historyRepo, favoriteRepository)
        val viewModel = SearchViewModel(
            ToggleFavoriteMovieUseCase(favoriteRepository),
            search,
            getText,
            clearSearchText,
            favoriteRepository,
            historyRepo,
            getHistory,
            mockk(relaxed = true)
        )

        viewModel.eventHandler(SearchViewModel.Event.QueryChanged("abc"))
        runCurrent()
        assertEquals(false, viewModel.state.value.showError)
        assertEquals(false, viewModel.state.value.showLoading)
        assertEquals(1, viewModel.state.value.results.size)

        viewModel.eventHandler(SearchViewModel.Event.QueryChanged(""))
        runCurrent()
        assertEquals("", viewModel.state.value.query)
    }

    @Test
    fun favoritesOverlay_updatesHeartAfterToggle() = runTest {
        val getText = GetSearchTextUseCase(object : SearchRepository {
            override val searchText = MutableStateFlow("")
            override fun setSearchText(text: String) {}
            override fun clearSearchText() {}
        })
        val search = mockk<SearchMoviesUseCase> {
            coEvery { this@mockk.invoke(any()) } returns Result.Success(listOf(createMovie(1)))
        }
        val clearSearchText = mockk<ClearSearchTextUseCase>()
        val favsFlow = MutableStateFlow(emptySet<Int>())
        val favoriteRepository = mockk<FavoriteMovieRepository> { coEvery { observeFavorites() } returns favsFlow }
        val historyRepo =
            mockk<SearchHistoryRepository> { coEvery { observeHistory() } returns MutableStateFlow(emptyList()) }
        val getHistory = GetSearchHistoryUseCase(historyRepo, favoriteRepository)
        val viewModel = SearchViewModel(
            ToggleFavoriteMovieUseCase(favoriteRepository),
            search,
            getText,
            clearSearchText,
            favoriteRepository,
            historyRepo,
            getHistory,
            mockk(relaxed = true)
        )

        viewModel.eventHandler(SearchViewModel.Event.QueryChanged("a"))
        runCurrent()
        assertEquals(false, viewModel.state.value.results.first().isFavorite)

        favsFlow.value = setOf(1)
        runCurrent()
        assertEquals(true, viewModel.state.value.results.first().isFavorite)
    }

    @Test
    fun queryChanged_setsLoadingTrue_thenSuccessResetsFlags() = runTest {
        val getText = GetSearchTextUseCase(object : SearchRepository {
            override val searchText = MutableStateFlow("")
            override fun setSearchText(text: String) {}
            override fun clearSearchText() {}
        })
        val clearSearchText = mockk<ClearSearchTextUseCase>()
        val search = mockk<SearchMoviesUseCase> {
            coEvery { this@mockk.invoke(any()) } coAnswers {
                delay(1)
                Result.Success(emptyList())
            }
        }
        val favoriteRepository =
            mockk<FavoriteMovieRepository> { coEvery { observeFavorites() } returns MutableStateFlow(emptySet()) }
        val historyRepo =
            mockk<SearchHistoryRepository> { coEvery { observeHistory() } returns MutableStateFlow(emptyList()) }
        val getHistory = GetSearchHistoryUseCase(historyRepo, favoriteRepository)
        val viewModel = SearchViewModel(
            ToggleFavoriteMovieUseCase(favoriteRepository),
            search,
            getText,
            clearSearchText,
            favoriteRepository,
            historyRepo,
            getHistory,
            mockk(relaxed = true)
        )

        viewModel.eventHandler(SearchViewModel.Event.QueryChanged("abc"))
        assertEquals(true, viewModel.state.value.showLoading)
        advanceUntilIdle()
        assertEquals(false, viewModel.state.value.showLoading)
        assertEquals(false, viewModel.state.value.showError)
    }

    @Test
    fun queryChanged_error_setsShowErrorTrue_andLoadingFalse() = runTest {
        val getText = GetSearchTextUseCase(object : SearchRepository {
            override val searchText = MutableStateFlow("")
            override fun setSearchText(text: String) {}
            override fun clearSearchText() {}
        })
        val clearSearchText = mockk<ClearSearchTextUseCase>()
        val search = mockk<SearchMoviesUseCase> {
            coEvery { this@mockk.invoke(any()) } returns Result.Error(Exception("boom"))
        }
        val favoriteRepository =
            mockk<FavoriteMovieRepository> { coEvery { observeFavorites() } returns MutableStateFlow(emptySet()) }
        val historyRepo =
            mockk<SearchHistoryRepository> { coEvery { observeHistory() } returns MutableStateFlow(emptyList()) }
        val getHistory = GetSearchHistoryUseCase(historyRepo, favoriteRepository)
        val viewModel = SearchViewModel(
            ToggleFavoriteMovieUseCase(favoriteRepository),
            search,
            getText,
            clearSearchText,
            favoriteRepository,
            historyRepo,
            getHistory,
            mockk(relaxed = true)
        )

        viewModel.eventHandler(SearchViewModel.Event.QueryChanged("abc"))
        runCurrent()
        assertEquals(true, viewModel.state.value.showError)
        assertEquals(false, viewModel.state.value.showLoading)
    }
}


