package xyz.kewiany.movieexplorer.data.repository

import kotlinx.coroutines.flow.Flow
import xyz.kewiany.movieexplorer.data.SearchTextHolder
import xyz.kewiany.movieexplorer.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchTextHolder: SearchTextHolder,
) : SearchRepository {

    override val searchText: Flow<String> = searchTextHolder.searchText

    override fun setSearchText(text: String) {
        searchTextHolder.setSearchText(text)
    }

    override fun clearSearchText() {
        searchTextHolder.setSearchText("")
    }
}