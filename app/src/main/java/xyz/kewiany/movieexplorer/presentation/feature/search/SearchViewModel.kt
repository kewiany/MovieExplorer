package xyz.kewiany.movieexplorer.presentation.feature.search

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.common.navigation.NavigationDirections
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.common.utils.SEARCH_DEBOUNCE_MS
import xyz.kewiany.movieexplorer.common.viewmodel.MoviesBaseViewModel
import xyz.kewiany.movieexplorer.domain.model.toSearchHistory
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import xyz.kewiany.movieexplorer.domain.repository.SearchHistoryRepository
import xyz.kewiany.movieexplorer.domain.usecase.ClearSearchTextUseCase
import xyz.kewiany.movieexplorer.domain.usecase.GetSearchHistoryUseCase
import xyz.kewiany.movieexplorer.domain.usecase.GetSearchTextUseCase
import xyz.kewiany.movieexplorer.domain.usecase.SearchMoviesUseCase
import xyz.kewiany.movieexplorer.domain.usecase.ToggleFavoriteMovieUseCase
import xyz.kewiany.movieexplorer.presentation.MovieUi
import xyz.kewiany.movieexplorer.presentation.feature.search.SearchViewModel.Event
import xyz.kewiany.movieexplorer.presentation.feature.search.SearchViewModel.State
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    toggleFavorite: ToggleFavoriteMovieUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getSearchTextUseCase: GetSearchTextUseCase,
    private val clearSearchTextUseCase: ClearSearchTextUseCase,
    private val favoriteRepository: FavoriteMovieRepository,
    private val historyRepository: SearchHistoryRepository,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val navigator: Navigator,
) : MoviesBaseViewModel<State, Event>(State(), toggleFavorite, navigator) {

    init {
        searchTextFlow().launchIn(viewModelScope)

        viewModelScope.launch {
            favoriteRepository.observeFavorites().collectLatest { favIds ->
                updateState { s -> s.copy(results = s.results.map { it.copy(isFavorite = favIds.contains(it.id)) }) }
            }
        }

        viewModelScope.launch {
            getSearchHistoryUseCase().collectLatest { items ->
                updateState { it.copy(history = items) }
            }
        }
    }

    private fun searchTextFlow() = getSearchTextUseCase()
        .debounce(SEARCH_DEBOUNCE_MS)
        .onEach { text -> eventHandler(Event.QueryChanged(text)) }

    override fun handleEvent(event: Event) = when (event) {
        is Event.ToggleFavorite -> handleToggleFavorite(event)
        is Event.QueryChanged -> handleQueryChanged(event)
        is Event.MovieClicked -> handleMovieClicked(event)
        Event.BackClicked -> handleBackCLicked()
    }

    private fun handleToggleFavorite(event: Event.ToggleFavorite) {
        toggleFavoriteMovie(event.id)
    }

    private fun handleQueryChanged(event: Event.QueryChanged) {
        val query = event.query
        updateState { it.copy(query = query, showError = false, showLoading = true) }
        viewModelScope.launch { searchMovies(query) }
    }

    private suspend fun searchMovies(query: String) {
        when (val result = searchMoviesUseCase(query)) {
            is Result.Success -> handleMovies(result.data)
            is Result.Error -> handleError()
        }
    }

    private fun handleMovies(data: List<MovieUi>) {
        updateState { it.copy(showError = false, showLoading = false, results = data) }
    }

    private fun handleError() {
        updateState { it.copy(showError = true, showLoading = false) }
    }

    private fun handleMovieClicked(event: Event.MovieClicked) {
        state.value.results.find { it.id == event.id }?.let { item ->
            viewModelScope.launch { historyRepository.add(item.toSearchHistory()) }
        }
        navigator.navigate(NavigationDirections.movieDetails(event.id))
    }

    private fun handleBackCLicked() {
        navigator.back()
    }

    override fun onCleared() {
        clearSearchTextUseCase()
        super.onCleared()
    }

    data class State(
        val query: String = "",
        val showError: Boolean = false,
        val showLoading: Boolean = false,
        val results: List<MovieUi> = emptyList(),
        val history: List<MovieUi> = emptyList(),
    )

    sealed class Event {
        data class QueryChanged(val query: String) : Event()
        data class MovieClicked(val id: Int) : Event()
        data class ToggleFavorite(val id: Int) : Event()
        data object BackClicked : Event()
    }
}