package xyz.kewiany.movieexplorer.domain.repository

import kotlinx.coroutines.flow.Flow
import xyz.kewiany.movieexplorer.domain.model.SearchHistory

interface SearchHistoryRepository {
    fun observeHistory(): Flow<List<SearchHistory>>
    suspend fun add(item: SearchHistory)
    suspend fun clear()
}


