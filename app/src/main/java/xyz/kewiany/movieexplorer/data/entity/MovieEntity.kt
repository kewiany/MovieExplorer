package xyz.kewiany.movieexplorer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double?,
    val voteCount: Int,
    val popularity: Double,
    val releaseDate: String?,
    val runtime: Int?,
)