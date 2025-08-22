package xyz.kewiany.movieexplorer.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface SearchTextHolder {
    val searchText: Flow<String>
    fun setSearchText(text: String)
}

class SearchTextHolderImpl @Inject constructor() : SearchTextHolder {

    private val _searchText = MutableStateFlow("")
    override val searchText: Flow<String> = _searchText

    override fun setSearchText(text: String) {
        _searchText.value = text
    }
}