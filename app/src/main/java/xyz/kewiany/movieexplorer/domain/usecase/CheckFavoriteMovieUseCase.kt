package xyz.kewiany.movieexplorer.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class CheckFavoriteMovieUseCase @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository,
) {

    suspend operator fun invoke(id: Int): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val isFavorite = favoriteMovieRepository.isFavorite(id)
            Result.Success(isFavorite)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(e)
        }
    }
}


