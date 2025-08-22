package xyz.kewiany.movieexplorer.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.kewiany.movieexplorer.data.dao.FavoriteDao
import xyz.kewiany.movieexplorer.data.entity.FavoriteEntity
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import javax.inject.Inject

class FavoriteMovieRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
) : FavoriteMovieRepository {

    override fun observeFavorites(): Flow<Set<Int>> =
        favoriteDao.observeAll().map { it.toSet() }

    override suspend fun toggleFavorite(id: Int) {
        if (favoriteDao.exists(id)) favoriteDao.delete(id) else favoriteDao.insert(FavoriteEntity(id))
    }

    override suspend fun isFavorite(id: Int): Boolean = favoriteDao.exists(id)
}


