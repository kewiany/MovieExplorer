package xyz.kewiany.movieexplorer.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import xyz.kewiany.movieexplorer.data.api.TMDBApi
import xyz.kewiany.movieexplorer.data.dao.MovieDao
import xyz.kewiany.movieexplorer.data.entity.MovieEntity
import xyz.kewiany.movieexplorer.data.response.MovieItemResponse
import xyz.kewiany.movieexplorer.domain.model.Movie

class PopularMoviesPagingSource(
    private val api: TMDBApi,
    private val movieDao: MovieDao,
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = api.getPopularMovies(page)
            val movies = response.results.map { it.toEntity().toDomain() }

            try {
                movieDao.upsertAll(response.results.map { it.toEntity() })
            } catch (_: Exception) { /* ignore write errors for UI flow */
            }

            val nextKey = if (page >= response.totalPages) null else page + 1
            val prevKey = if (page == 1) null else page - 1
            LoadResult.Page(
                data = movies,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

private fun MovieItemResponse.toEntity() = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    releaseDate = releaseDate,
    runtime = null,
)

private fun MovieEntity.toDomain() = Movie(
    id = id,
    title = title,
    posterPath = posterPath,
    releaseDate = releaseDate ?: "",
)


