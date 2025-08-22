package xyz.kewiany.movieexplorer.presentation.feature.languages

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import xyz.kewiany.movieexplorer.R

@Composable
fun MovieLanguagesScreen(
    movieId: Int,
    state: State<MovieLanguagesViewModel.State>,
    eventHandler: (MovieLanguagesViewModel.Event) -> Unit,
) {
    LaunchedEffect(movieId) {
        eventHandler(MovieLanguagesViewModel.Event.Load(movieId))
    }

    AlertDialog(
        onDismissRequest = { eventHandler(MovieLanguagesViewModel.Event.OutsideClicked) },
        confirmButton = {
            TextButton(onClick = {
                eventHandler(MovieLanguagesViewModel.Event.CloseClicked)
            }) {
                Text(stringResource(R.string.close))
            }
        },
        title = { Text(stringResource(R.string.spoken_languages)) },
        text = {
            val content = if (state.value.list.isEmpty()) {
                stringResource(R.string.no_languages)
            } else {
                state.value.list.joinToString("\n")
            }
            Text(content)
        }
    )
}



