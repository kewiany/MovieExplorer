package xyz.kewiany.movieexplorer.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoriteMovieRepository {
    fun observeFavorites(): Flow<Set<Int>>
    suspend fun toggleFavorite(id: Int)
    suspend fun isFavorite(id: Int): Boolean
}


