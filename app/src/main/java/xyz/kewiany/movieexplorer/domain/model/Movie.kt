package xyz.kewiany.movieexplorer.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val releaseDate: String,
)


