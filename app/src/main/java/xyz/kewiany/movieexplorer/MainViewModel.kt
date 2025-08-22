package xyz.kewiany.movieexplorer

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import xyz.kewiany.movieexplorer.common.navigation.NavigationDirections
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.common.viewmodel.BaseViewModel
import xyz.kewiany.movieexplorer.domain.usecase.ClearSearchTextUseCase
import xyz.kewiany.movieexplorer.domain.usecase.GetSearchTextUseCase
import xyz.kewiany.movieexplorer.domain.usecase.SetSearchTextUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val clearSearchTextUseCase: ClearSearchTextUseCase,
    private val getSearchTextUseCase: GetSearchTextUseCase,
    private val setSearchTextUseCase: SetSearchTextUseCase,
    val navigator: Navigator,
) : BaseViewModel<MainViewModel.State, MainViewModel.Event>(State()) {

    companion object {
        private val ROUTES_WITH_TOP_BAR = setOf(
            NavigationDirections.popularMovies.destination,
            NavigationDirections.favorites.destination,
            NavigationDirections.search.destination,
            "movie_details/{movieId}",
            "movie_languages/{movieId}"
        )

        private val ROUTES_WITH_FILTER = setOf(
            NavigationDirections.popularMovies.destination,
            NavigationDirections.favorites.destination,
        )

        private val ROUTES_WITH_BACK_BUTTON = setOf(
            NavigationDirections.search.destination,
            "movie_details/{movieId}",
            "movie_languages/{movieId}"
        )

        private val ROUTES_WITH_SEARCH_FIELD = setOf(
            NavigationDirections.search.destination
        )

        private val ROUTES_WITH_SEARCH_BUTTON = setOf(
            NavigationDirections.popularMovies.destination,
            NavigationDirections.favorites.destination,
        )
    }

    init {
        searchTextFlow().launchIn(viewModelScope)
    }

    private fun searchTextFlow() = getSearchTextUseCase()
        .onEach { text -> updateState { it.copy(searchText = text) } }

    override fun handleEvent(event: Event) = when (event) {
        Event.SearchClicked -> handleSearchClicked()
        Event.ShowAllClicked -> handleShowAllClicked()
        Event.ShowFavoritesClicked -> handleShowFavoritesClicked()
        Event.ClearSearchClicked -> handleClearSearchClicked()
        is Event.SearchTextChanged -> handleSearchTextChanged(event)
        is Event.SearchFocused -> handleSearchFocused(event)
        is Event.SetCurrentRoute -> handleCurrentRoute(event)
    }

    private fun handleSearchClicked() {
        navigator.navigate(NavigationDirections.search)
    }

    private fun handleShowAllClicked() {
        navigator.back()
        updateState { it.copy(showFavoritesMovies = false) }
    }

    private fun handleShowFavoritesClicked() {
        navigator.navigate(NavigationDirections.favorites)
        updateState { it.copy(showFavoritesMovies = true) }
    }

    private fun handleClearSearchClicked() {
        clearSearchTextUseCase()
    }

    private fun handleSearchTextChanged(event: Event.SearchTextChanged) {
        val text = event.text
        setSearchTextUseCase(text)
    }

    private fun handleSearchFocused(event: Event.SearchFocused) {
        updateState { it.copy(showClearButton = event.isFocused) }
    }

    private fun handleCurrentRoute(event: Event.SetCurrentRoute) {
        val currentRoute = event.route
        updateState {
            it.copy(
                currentRoute = currentRoute,
                topBarVisible = currentRoute in ROUTES_WITH_TOP_BAR,
                showSearchField = currentRoute in ROUTES_WITH_SEARCH_FIELD,
                showFilterIcon = currentRoute in ROUTES_WITH_FILTER,
                showSearchIcon = currentRoute in ROUTES_WITH_SEARCH_BUTTON,
                showBackButton = currentRoute in ROUTES_WITH_BACK_BUTTON
            )
        }
    }

    data class State(
        val currentRoute: String? = null,
        val topBarVisible: Boolean = false,
        val showFilterIcon: Boolean = false,
        val showSearchIcon: Boolean = false,
        val showSearchField: Boolean = false,
        val showBackButton: Boolean = false,
        val showFavoritesMovies: Boolean = false,
        val searchText: String = "",
        val showClearButton: Boolean = false,
    )

    sealed class Event {
        data object SearchClicked : Event()
        data object ShowAllClicked : Event()
        data object ShowFavoritesClicked : Event()
        data object ClearSearchClicked : Event()
        data class SearchFocused(val isFocused: Boolean) : Event()
        data class SearchTextChanged(val text: String) : Event()
        data class SetCurrentRoute(val route: String?) : Event()
    }
}