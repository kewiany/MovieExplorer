package xyz.kewiany.movieexplorer.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class FetchSpokenLanguagesUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(id: Int): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            Result.Success(repository.fetchSpokenLanguages(id))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(e)
        }
    }
}



