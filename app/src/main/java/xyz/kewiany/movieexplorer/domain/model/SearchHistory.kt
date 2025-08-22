package xyz.kewiany.movieexplorer.domain.model

import xyz.kewiany.movieexplorer.presentation.MovieUi

data class SearchHistory(
    val id: Int,
    val title: String,
    val posterPath: String?,
)

fun MovieUi.toSearchHistory() = SearchHistory(
    id = id,
    title = title,
    posterPath = posterPath,
)