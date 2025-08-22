package xyz.kewiany.movieexplorer.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.kewiany.movieexplorer.data.dao.SearchHistoryDao
import xyz.kewiany.movieexplorer.data.entity.SearchHistoryEntity
import xyz.kewiany.movieexplorer.domain.model.SearchHistory
import xyz.kewiany.movieexplorer.domain.repository.SearchHistoryRepository
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val dao: SearchHistoryDao,
) : SearchHistoryRepository {
    override fun observeHistory(): Flow<List<SearchHistory>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun add(item: SearchHistory) {
        dao.insert(
            SearchHistoryEntity(
                movieId = item.id,
                title = item.title,
                posterPath = item.posterPath,
                clickedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun clear() {
        dao.clear()
    }
}

private fun SearchHistoryEntity.toDomain() = SearchHistory(
    id = id,
    title = title,
    posterPath = posterPath,
)
