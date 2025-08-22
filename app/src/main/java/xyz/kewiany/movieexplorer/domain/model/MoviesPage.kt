package xyz.kewiany.movieexplorer.domain.model

data class MoviesPage(
    val movies: List<Movie>,
    val currentPage: Int,
    val totalPages: Int,
    val totalResults: Int,
)


