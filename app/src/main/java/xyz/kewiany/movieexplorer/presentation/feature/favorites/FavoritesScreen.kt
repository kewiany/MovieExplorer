package xyz.kewiany.movieexplorer.presentation.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import xyz.kewiany.movieexplorer.presentation.feature.favorites.FavoritesViewModel.Event
import xyz.kewiany.movieexplorer.presentation.feature.popular.MovieItem

@Composable
fun FavoritesScreen(
    state: State<FavoritesViewModel.State>,
    eventHandler: (Event) -> Unit,
) {
    if (state.value.isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        return
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(state.value.movies, key = { _, item -> item.id }) { _, movie ->
            MovieItem(
                movie = movie,
                onClick = {
                    eventHandler(Event.MovieClicked(movie.id))
                },
                onToggleFavorite = {
                    eventHandler(Event.ToggleFavorite(movie.id))
                },
                isFavorite = movie.isFavorite
            )
        }
    }
}


