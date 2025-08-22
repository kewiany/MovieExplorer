@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package xyz.kewiany.movieexplorer.domain.usecase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.kewiany.menusy.test.common.BaseTest
import xyz.kewiany.movieexplorer.domain.repository.SearchRepository

class SearchTextUseCasesTest : BaseTest() {

    @Test
    fun set_and_get_and_clear_search_text() = runTest {
        val store = object : SearchRepository {
            override val searchText = MutableStateFlow("")
            override fun setSearchText(text: String) {
                searchText.value = text
            }

            override fun clearSearchText() {
                searchText.value = ""
            }
        }

        val set = SetSearchTextUseCase(store)
        val get = GetSearchTextUseCase(store)
        val clear = ClearSearchTextUseCase(store)

        set("abc")
        assertEquals("abc", get().first())
        clear()
        assertEquals("", get().first())
    }
}


