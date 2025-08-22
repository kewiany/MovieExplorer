package xyz.kewiany.movieexplorer.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import xyz.kewiany.movieexplorer.domain.repository.SearchHistoryRepository
import xyz.kewiany.movieexplorer.presentation.MovieUi
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val historyRepository: SearchHistoryRepository,
    private val favoriteRepository: FavoriteMovieRepository,
) {
    operator fun invoke(): Flow<List<MovieUi>> =
        combine(favoriteRepository.observeFavorites(), historyRepository.observeHistory()) { favIds, items ->
            items.map { history ->
                MovieUi(
                    id = history.id,
                    title = history.title,
                    posterPath = history.posterPath,
                    isFavorite = favIds.contains(history.id)
                )
            }
        }
}


