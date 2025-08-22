@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.menusy.test.common.BaseTest
import xyz.kewiany.movieexplorer.data.SearchTextHolder

class SearchRepositoryImplTest : BaseTest() {

    @Test
    fun set_and_clear_propagateToHolder() = runTest {
        val holder = object : SearchTextHolder {
            override val searchText = MutableStateFlow("")
            override fun setSearchText(text: String) {
                searchText.value = text
            }
        }
        val repo = SearchRepositoryImpl(holder)
        repo.setSearchText("abc")
        assertEquals("abc", repo.searchText.first())
        repo.clearSearchText()
        assertEquals("", repo.searchText.first())
    }
}


