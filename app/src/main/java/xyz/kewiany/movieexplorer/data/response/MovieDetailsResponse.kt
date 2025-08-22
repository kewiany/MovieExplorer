package xyz.kewiany.movieexplorer.data.response

import com.squareup.moshi.Json

data class MovieDetailsResponse(
    val adult: Boolean,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "belongs_to_collection")
    val belongsToCollection: CollectionResponse?,
    val budget: Long,
    val genres: List<GenreResponse>,
    val homepage: String?,
    val id: Int,
    @Json(name = "imdb_id")
    val imdbId: String?,
    @Json(name = "origin_country")
    val originCountry: List<String>,
    @Json(name = "original_language")
    val originalLanguage: String,
    @Json(name = "original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "production_companies")
    val productionCompanies: List<ProductionCompanyResponse>,
    @Json(name = "production_countries")
    val productionCountries: List<ProductionCountryResponse>,
    @Json(name = "release_date")
    val releaseDate: String,
    val revenue: Long,
    val runtime: Int?,
    @Json(name = "spoken_languages")
    val spokenLanguages: List<SpokenLanguageResponse>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    @Json(name = "vote_average")
    val voteAverage: Double,
    @Json(name = "vote_count")
    val voteCount: Int,
)

data class SpokenLanguageResponse(
    @Json(name = "english_name") val englishName: String,
    @Json(name = "iso_639_1") val iso6391: String,
    val name: String,
)

data class GenreResponse(
    val id: Int,
    val name: String,
)

data class CollectionResponse(
    val id: Int,
    val name: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
)

data class ProductionCompanyResponse(
    val id: Int,
    @Json(name = "logo_path") val logoPath: String?,
    val name: String,
    @Json(name = "origin_country") val originCountry: String,
)

data class ProductionCountryResponse(
    @Json(name = "iso_3166_1") val iso31661: String,
    val name: String,
)


