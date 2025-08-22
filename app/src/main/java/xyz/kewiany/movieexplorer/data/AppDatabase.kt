package xyz.kewiany.movieexplorer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.kewiany.movieexplorer.data.dao.FavoriteDao
import xyz.kewiany.movieexplorer.data.dao.MovieDao
import xyz.kewiany.movieexplorer.data.dao.SearchHistoryDao
import xyz.kewiany.movieexplorer.data.entity.FavoriteEntity
import xyz.kewiany.movieexplorer.data.entity.MovieEntity
import xyz.kewiany.movieexplorer.data.entity.SearchHistoryEntity

@Database(
    entities = [MovieEntity::class, FavoriteEntity::class, SearchHistoryEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}

