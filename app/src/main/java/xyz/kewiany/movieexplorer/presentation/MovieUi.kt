package xyz.kewiany.movieexplorer.presentation

data class MovieUi(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val isFavorite: Boolean,
)