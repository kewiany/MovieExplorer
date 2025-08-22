package xyz.kewiany.movieexplorer.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import xyz.kewiany.movieexplorer.data.response.MovieDetailsResponse
import xyz.kewiany.movieexplorer.data.response.PopularMoviesResponse
import xyz.kewiany.movieexplorer.data.response.SearchMoviesResponse

interface TMDBApi {

    @GET("discover/movie")
    suspend fun getPopularMovies(@Query("page") page: Int): PopularMoviesResponse

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String, @Query("page") page: Int): SearchMoviesResponse

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDetailsResponse
}


