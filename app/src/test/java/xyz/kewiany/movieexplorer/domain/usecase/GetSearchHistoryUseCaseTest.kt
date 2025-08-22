@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.menusy.test.common.BaseTest
import xyz.kewiany.movieexplorer.domain.model.SearchHistory
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import xyz.kewiany.movieexplorer.domain.repository.SearchHistoryRepository

class GetSearchHistoryUseCaseTest : BaseTest() {

    @Test
    fun overlaysFavoritesAndMapsFields() = runTest {
        val historyItems = listOf(
            SearchHistory(1, "A", "p1"),
            SearchHistory(2, "B", "p2"),
        )
        val historyRepo = mockk<SearchHistoryRepository> {
            coEvery { observeHistory() } returns MutableStateFlow(historyItems)
        }
        val favRepo = mockk<FavoriteMovieRepository> {
            coEvery { observeFavorites() } returns MutableStateFlow(setOf(2))
        }

        val useCase = GetSearchHistoryUseCase(historyRepo, favRepo)
        val list = useCase().first()

        assertEquals(listOf(1, 2), list.map { it.id })
        assertEquals(listOf(false, true), list.map { it.isFavorite })
        assertEquals(listOf("A", "B"), list.map { it.title })
        assertEquals(listOf("p1", "p2"), list.map { it.posterPath })
    }
}


