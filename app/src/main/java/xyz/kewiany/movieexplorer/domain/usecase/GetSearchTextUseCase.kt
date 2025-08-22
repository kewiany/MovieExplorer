package xyz.kewiany.movieexplorer.domain.usecase

import kotlinx.coroutines.flow.Flow
import xyz.kewiany.movieexplorer.domain.repository.SearchRepository
import javax.inject.Inject

class GetSearchTextUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    operator fun invoke(): Flow<String> {
        return searchRepository.searchText
    }
}