package xyz.kewiany.movieexplorer

import xyz.kewiany.movieexplorer.data.entity.MovieEntity
import xyz.kewiany.movieexplorer.data.response.MovieDetailsResponse
import xyz.kewiany.movieexplorer.data.response.MovieItemResponse
import xyz.kewiany.movieexplorer.domain.model.MovieDetails
import xyz.kewiany.movieexplorer.presentation.MovieUi

fun createMovie(
    id: Int = 1,
    title: String = "title",
    posterPath: String = "posterPath",
    isFavorite: Boolean = false,
) = MovieUi(
    id = id,
    title = title,
    posterPath = posterPath,
    isFavorite = isFavorite
)

fun createMovieDetails(
    id: Int = 1,
    title: String = "title",
    overview: String = "overview",
    posterPath: String? = null,
    releaseDate: String = "releaseDate",
    voteAverage: Double = 0.0,
    voteCount: Int = 0,
    runtime: Int? = 0,
) = MovieDetails(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    runtime = runtime,
)

fun createMovieItemResponse(id: Int) = MovieItemResponse(
    adult = false,
    backdropPath = "null",
    genreIds = emptyList(),
    id = id,
    originalLanguage = "originalLanguage",
    originalTitle = "originalTitle",
    overview = "overview",
    popularity = 0.0,
    posterPath = "posterPath",
    releaseDate = "releaseDate",
    title = "title",
    video = false,
    voteAverage = 0.0,
    voteCount = 0,
)

fun createMovieDetailsResponse(id: Int) = MovieDetailsResponse(
    adult = false,
    backdropPath = "backdropPath",
    belongsToCollection = null,
    budget = 0,
    genres = emptyList(),
    homepage = "homepage",
    id = id,
    imdbId = "imdbId",
    originalLanguage = "originalLanguage",
    originalTitle = "originalTitle",
    overview = "overview",
    popularity = 0.0,
    posterPath = null,
    productionCompanies = emptyList(),
    productionCountries = emptyList(),
    releaseDate = "releaseDate",
    revenue = 0,
    runtime = null,
    spokenLanguages = emptyList(),
    status = "status",
    tagline = "tagline",
    title = "title",
    video = false,
    voteAverage = 0.0,
    voteCount = 0,
    originCountry = emptyList()
)

fun createMovieEntity(id: Int) = MovieEntity(
    id = id,
    title = "title",
    overview = "overview",
    posterPath = "posterPath",
    backdropPath = "backdropPath",
    voteAverage = 7.5,
    voteCount = 10,
    popularity = 99.0,
    releaseDate = "releaseDate",
    runtime = null,
)