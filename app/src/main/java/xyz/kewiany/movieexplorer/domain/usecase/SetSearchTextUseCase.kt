package xyz.kewiany.movieexplorer.domain.usecase

import xyz.kewiany.movieexplorer.domain.repository.SearchRepository
import javax.inject.Inject

class SetSearchTextUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    operator fun invoke(text: String) {
        searchRepository.setSearchText(text)
    }
}