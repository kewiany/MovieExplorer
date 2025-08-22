@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.data.repository

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.movieexplorer.data.dao.FavoriteDao
import xyz.kewiany.movieexplorer.data.entity.FavoriteEntity

class FavoriteMovieRepositoryImplTest {

    @Test
    fun when_observeFavorites_then_mapsToSet() = runTest {
        val dao = mockk<FavoriteDao> {
            coEvery { observeAll() } returns MutableStateFlow(listOf(1, 2, 2))
        }
        val repository = FavoriteMovieRepositoryImpl(dao)

        val result = repository.observeFavorites().first()
        assertEquals(setOf(1, 2), result)
    }

    @Test
    fun when_toggleFavorite_notExists_then_insert() = runTest {
        val dao = mockk<FavoriteDao> {
            coEvery { exists(5) } returns false
            coJustRun { insert(FavoriteEntity(5)) }
        }
        val repository = FavoriteMovieRepositoryImpl(dao)

        repository.toggleFavorite(5)
        coVerify { dao.insert(FavoriteEntity(5)) }
    }

    @Test
    fun when_toggleFavorite_exists_then_delete() = runTest {
        val dao = mockk<FavoriteDao> {
            coEvery { exists(7) } returns true
            coJustRun { delete(7) }
        }
        val repository = FavoriteMovieRepositoryImpl(dao)

        repository.toggleFavorite(7)
        coVerify { dao.delete(7) }
    }

    @Test
    fun when_isFavorite_delegatesToDao() = runTest {
        val dao = mockk<FavoriteDao> {
            coEvery { exists(1) } returns true
        }
        val repository = FavoriteMovieRepositoryImpl(dao)

        val isFav = repository.isFavorite(1)
        assertEquals(true, isFav)
    }
}


