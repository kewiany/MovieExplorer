package xyz.kewiany.movieexplorer.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import xyz.kewiany.movieexplorer.domain.model.Movie
import xyz.kewiany.movieexplorer.domain.model.MovieDetails
import xyz.kewiany.movieexplorer.domain.model.MoviesPage

interface MovieRepository {
    fun pagerPopularMovies(): Flow<PagingData<Movie>>
    fun observeAllMovies(): Flow<List<Movie>>

    suspend fun searchMovies(query: String, page: Int): MoviesPage

    fun observeMovieDetails(id: Int): Flow<MovieDetails>
    suspend fun fetchMovieDetails(id: Int)

    suspend fun fetchOverview(id: Int): String
    suspend fun fetchReleaseDate(id: Int): String
    suspend fun fetchSpokenLanguages(id: Int): List<String>
}


