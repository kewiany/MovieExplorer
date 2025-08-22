@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.menusy.test.common.BaseTest
import xyz.kewiany.movieexplorer.data.dao.SearchHistoryDao
import xyz.kewiany.movieexplorer.data.entity.SearchHistoryEntity
import xyz.kewiany.movieexplorer.domain.model.SearchHistory

class SearchHistoryRepositoryImplTest : BaseTest() {

    @Test
    fun observeHistory_mapsEntitiesToDomain() = runTest {
        val dao = mockk<SearchHistoryDao> {
            coEvery { observeAll() } returns MutableStateFlow(
                listOf(
                    SearchHistoryEntity(id = 1, movieId = 10, title = "A", posterPath = "p", clickedAt = 1L)
                )
            )
        }
        val repo = SearchHistoryRepositoryImpl(dao)
        val list = repo.observeHistory().first()
        assertEquals(1, list.size)
        assertEquals(1, list.first().id)
        assertEquals("A", list.first().title)
    }

    @Test
    fun add_insertsEntityWithCurrentTimestamp() = runTest {
        val captured = mutableListOf<SearchHistoryEntity>()
        val dao = object : SearchHistoryDao {
            override fun observeAll() = MutableStateFlow(emptyList<SearchHistoryEntity>())
            override suspend fun insert(entity: SearchHistoryEntity) {
                captured.add(entity)
            }

            override suspend fun clear() {}
        }
        val repo = SearchHistoryRepositoryImpl(dao)
        repo.add(SearchHistory(5, "T", "p"))
        assertEquals(1, captured.size)
        assertEquals(5, captured.first().movieId)
        assertEquals("T", captured.first().title)
    }
}


