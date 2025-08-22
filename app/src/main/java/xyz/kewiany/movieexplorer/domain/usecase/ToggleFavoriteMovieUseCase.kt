package xyz.kewiany.movieexplorer.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ToggleFavoriteMovieUseCase @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository,
) {
    suspend operator fun invoke(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            favoriteMovieRepository.toggleFavorite(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(e)
        }
    }
}


