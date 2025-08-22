package xyz.kewiany.movieexplorer.presentation.feature.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import xyz.kewiany.movieexplorer.R
import xyz.kewiany.movieexplorer.common.utils.TMDB_IMAGE_BASE_URL

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    state: State<MovieDetailsViewModel.State>,
    eventHandler: (MovieDetailsViewModel.Event) -> Unit,
) {
    val movie = state.value.movie
    LaunchedEffect(movieId) {
        if (movie == null) {
            eventHandler(MovieDetailsViewModel.Event.Load(movieId))
        }
    }

    if (movie == null) {
        return
    }

    Column(modifier = Modifier.padding(16.dp)) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                .data(movie.posterPath?.let { TMDB_IMAGE_BASE_URL + it })
                .crossfade(true)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .scale(Scale.FILL)
                .build()
        )
        Image(
            painter = painter,
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = movie.title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.overview.ifEmpty { stringResource(R.string.no_overview) },
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = {
            eventHandler(MovieDetailsViewModel.Event.ToggleFavorite(movie.id))
        }) {
            if (state.value.isFavorite) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = stringResource(R.string.unfavorite)
                )
            } else {
                Icon(
                    Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(R.string.favorite)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.release, movie.releaseDate),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                eventHandler(MovieDetailsViewModel.Event.ShowLanguages(movie.id))
            }) {
                Text(text = stringResource(R.string.languages))
            }
        }
    }
}