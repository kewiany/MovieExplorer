package xyz.kewiany.movieexplorer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.kewiany.movieexplorer.common.utils.PAGING_INITIAL_LOAD_SIZE
import xyz.kewiany.movieexplorer.common.utils.PAGING_PAGE_SIZE
import xyz.kewiany.movieexplorer.data.api.TMDBApi
import xyz.kewiany.movieexplorer.data.dao.MovieDao
import xyz.kewiany.movieexplorer.data.entity.MovieEntity
import xyz.kewiany.movieexplorer.data.paging.PopularMoviesPagingSource
import xyz.kewiany.movieexplorer.data.response.MovieDetailsResponse
import xyz.kewiany.movieexplorer.data.response.MovieItemResponse
import xyz.kewiany.movieexplorer.domain.model.Movie
import xyz.kewiany.movieexplorer.domain.model.MovieDetails
import xyz.kewiany.movieexplorer.domain.model.MoviesPage
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val api: TMDBApi,
) : MovieRepository {

    override fun pagerPopularMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = PAGING_PAGE_SIZE, initialLoadSize = PAGING_INITIAL_LOAD_SIZE),
            pagingSourceFactory = { PopularMoviesPagingSource(api, movieDao) }
        ).flow.map { paging -> paging.map { entity -> entity } }

    override fun observeAllMovies(): Flow<List<Movie>> =
        movieDao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun searchMovies(query: String, page: Int): MoviesPage {
        val response = api.searchMovies(query, page)
        return MoviesPage(
            movies = response.results.map { it.toEntity().toDomain() },
            currentPage = response.page,
            totalPages = response.totalPages,
            totalResults = response.totalResults,
        )
    }

    override fun observeMovieDetails(id: Int): Flow<MovieDetails> =
        movieDao.observeById(id).map { entity ->
            MovieDetails(
                id = entity.id,
                title = entity.title,
                overview = entity.overview ?: "",
                posterPath = entity.posterPath,
                releaseDate = entity.releaseDate ?: "",
                voteAverage = entity.voteAverage ?: 0.0,
                voteCount = 0,
                runtime = entity.runtime,
            )
        }

    override suspend fun fetchMovieDetails(id: Int) {
        val response = api.getMovieDetails(id)
        movieDao.upsert(response.toEntity())
    }

    override suspend fun fetchOverview(id: Int): String {
        val response = api.getMovieDetails(id)
        return response.overview
    }

    override suspend fun fetchReleaseDate(id: Int): String {
        val response = api.getMovieDetails(id)
        return response.releaseDate
    }

    override suspend fun fetchSpokenLanguages(id: Int): List<String> {
        val response = api.getMovieDetails(id)
        return response.spokenLanguages.map { it.englishName }
    }
}

private fun MovieEntity.toDomain() = Movie(
    id = id,
    title = title,
    posterPath = posterPath,
    releaseDate = releaseDate ?: "",
)

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

private fun MovieDetailsResponse.toEntity() = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    releaseDate = releaseDate,
    runtime = runtime
)



