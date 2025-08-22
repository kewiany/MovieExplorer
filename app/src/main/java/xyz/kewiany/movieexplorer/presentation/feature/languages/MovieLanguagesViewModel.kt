package xyz.kewiany.movieexplorer.presentation.feature.languages

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.common.viewmodel.BaseViewModel
import xyz.kewiany.movieexplorer.domain.usecase.FetchSpokenLanguagesUseCase
import javax.inject.Inject

@HiltViewModel
class MovieLanguagesViewModel @Inject constructor(
    private val navigator: Navigator,
    private val fetchSpokenLanguages: FetchSpokenLanguagesUseCase,
) : BaseViewModel<MovieLanguagesViewModel.State, MovieLanguagesViewModel.Event>(State()) {

    override fun handleEvent(event: Event) = when (event) {
        is Event.Load -> handleLoad(event)
        Event.OutsideClicked -> handleOutsideClicked()
        Event.CloseClicked -> handleCloseClicked()
    }

    private fun handleLoad(event: Event.Load) {
        val movieId = event.movieId
        viewModelScope.launch {
            when (val result = fetchSpokenLanguages(movieId)) {
                is Result.Success -> updateState { it.copy(list = result.data) }
                is Result.Error -> updateState { it.copy(list = emptyList()) }
            }
        }
    }

    private fun handleOutsideClicked() {
        navigator.back()
    }

    private fun handleCloseClicked() {
        navigator.back()
    }

    data class State(
        val list: List<String> = emptyList(),
    )

    sealed class Event {
        data class Load(val movieId: Int) : Event()
        data object OutsideClicked : Event()
        data object CloseClicked : Event()
    }
}


