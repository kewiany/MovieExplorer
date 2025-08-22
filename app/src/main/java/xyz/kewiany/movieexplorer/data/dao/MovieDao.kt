package xyz.kewiany.movieexplorer.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import xyz.kewiany.movieexplorer.data.entity.MovieEntity


@Dao
interface MovieDao {
    @Query("SELECT * FROM movies ORDER BY id DESC")
    fun observeAll(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    fun observeById(id: Int): Flow<MovieEntity>

    @Upsert
    suspend fun upsertAll(movies: List<MovieEntity>)

    @Upsert
    suspend fun upsert(movie: MovieEntity)

    @Query("DELETE FROM movies")
    suspend fun clearAll()
}


