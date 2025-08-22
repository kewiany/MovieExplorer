package xyz.kewiany.movieexplorer.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.domain.model.MovieDetails
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(id: Int): Result<MovieDetails> = withContext(Dispatchers.IO) {
        try {
            movieRepository.fetchMovieDetails(id)
            val data = movieRepository.observeMovieDetails(id).first()
            Result.Success(data)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(e)
        }
    }
}


