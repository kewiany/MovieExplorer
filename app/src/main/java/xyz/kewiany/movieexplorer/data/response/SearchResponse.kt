package xyz.kewiany.movieexplorer.data.response

import com.squareup.moshi.Json

data class SearchMoviesResponse(
    val page: Int,
    val results: List<MovieItemResponse>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int,
)


