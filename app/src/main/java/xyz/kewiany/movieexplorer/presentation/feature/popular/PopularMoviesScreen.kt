package xyz.kewiany.movieexplorer.presentation.feature.popular

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import xyz.kewiany.movieexplorer.R
import xyz.kewiany.movieexplorer.common.utils.TMDB_IMAGE_BASE_URL
import xyz.kewiany.movieexplorer.presentation.MovieUi

@Composable
fun PopularMoviesScreen(
    state: State<PopularMoviesViewModel.State>,
    eventHandler: (PopularMoviesViewModel.Event) -> Unit,
) {
    if (state.value.isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        return
    }
    val items = state.value.moviesPaged?.collectAsLazyPagingItems()
    val gridState = rememberSaveable(key = "popular_grid_state", saver = LazyGridState.Saver) { LazyGridState() }
    if (items != null) {
        PagedMovieGrid(
            items = items,
            gridState = gridState,
            onMovieClicked = { id -> eventHandler(PopularMoviesViewModel.Event.MovieClicked(id)) },
            onToggleFavoriteClicked = { id -> eventHandler(PopularMoviesViewModel.Event.ToggleFavorite(id)) },
        )
        return
    }
}

@Composable
private fun PagedMovieGrid(
    items: LazyPagingItems<MovieUi>,
    gridState: LazyGridState,
    onMovieClicked: (Int) -> Unit,
    onToggleFavoriteClicked: (Int) -> Unit,
) {
    when (items.loadState.refresh) {
        is LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return
        }
        is LoadState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                IconButton(onClick = { items.retry() }) {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = stringResource(R.string.retry)
                    )
                }
            }
            return
        }
        else -> {}
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = gridState,
    ) {
        items(
            count = items.itemCount,
            key = { index ->
                val id = items.peek(index)?.id
                if (id != null) "movie-$id-$index" else "idx-$index"
            }
        ) { index ->
            val movie = items[index] ?: return@items
            val isFavorite = movie.isFavorite
            MovieItem(
                movie = movie.copy(isFavorite = isFavorite),
                onClick = onMovieClicked,
                onToggleFavorite = onToggleFavoriteClicked,
                isFavorite = isFavorite,
            )
        }

        when (items.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }
            }
            is LoadState.Error -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { items.retry() }) {
                            Icon(
                                Icons.Outlined.FavoriteBorder,
                                contentDescription = "Retry"
                            )
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun MovieItem(
    movie: MovieUi,
    onClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    isFavorite: Boolean,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
            .data(movie.posterPath?.let { TMDB_IMAGE_BASE_URL + it })
            .crossfade(true)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .scale(Scale.FILL)
            .build()
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clickable { onClick(movie.id) },
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = { onToggleFavorite(movie.id) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(6.dp)
                .size(32.dp)
        ) {
            val icon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
            Icon(icon, contentDescription = "Toggle favorite")
        }
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 4.dp)
        )
    }
}