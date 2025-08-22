package xyz.kewiany.movieexplorer.presentation.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import xyz.kewiany.movieexplorer.presentation.feature.popular.MovieItem
import xyz.kewiany.movieexplorer.presentation.feature.search.SearchViewModel.Event.MovieClicked
import xyz.kewiany.movieexplorer.presentation.feature.search.SearchViewModel.Event.ToggleFavorite

@Composable
fun SearchScreen(
    state: State<SearchViewModel.State>,
    eventHandler: (SearchViewModel.Event) -> Unit,
) {
    if (state.value.showLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        return
    }

    if (state.value.query.isEmpty() && state.value.history.isNotEmpty()) {
        HistoryList(state = state, eventHandler = eventHandler)
        return
    }

    val results = state.value.results
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(count = results.size, key = { index -> "search-${results[index].id}-$index" }) { index ->
            val movie = results[index]
            MovieItem(
                movie = movie,
                onClick = { eventHandler(MovieClicked(movie.id)) },
                onToggleFavorite = { eventHandler(ToggleFavorite(movie.id)) },
                isFavorite = movie.isFavorite
            )
        }
    }
}

@Composable
private fun HistoryList(
    state: State<SearchViewModel.State>,
    eventHandler: (SearchViewModel.Event) -> Unit,
) {
    val history = state.value.history
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(count = history.size, key = { index -> "history-${history[index].id}-$index" }) { index ->
            val movie = history[index]
            MovieItem(
                movie = movie,
                onClick = { eventHandler(MovieClicked(movie.id)) },
                onToggleFavorite = { eventHandler(ToggleFavorite(movie.id)) },
                isFavorite = movie.isFavorite
            )
        }
    }
}