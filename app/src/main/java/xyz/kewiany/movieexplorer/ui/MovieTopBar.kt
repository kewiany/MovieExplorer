package xyz.kewiany.movieexplorer.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import xyz.kewiany.movieexplorer.MainViewModel
import xyz.kewiany.movieexplorer.MainViewModel.Event
import xyz.kewiany.movieexplorer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieTopBar(
    navController: NavHostController = rememberNavController(),
    state: State<MainViewModel.State>,
    isOnlyFavorites: Boolean = false,
    eventHandler: (Event) -> Unit,
) {
    if (state.value.topBarVisible) {
        TopAppBar(
            title = {
                if (state.value.showSearchField) {
                    val keyboard = LocalSoftwareKeyboardController.current
                    TextField(
                        value = state.value.searchText,
                        onValueChange = { q -> eventHandler(Event.SearchTextChanged(q)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.search_movies)) },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onAny = { keyboard?.hide() }),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (state.value.searchText.isNotEmpty()) {
                                IconButton(onClick = { eventHandler(Event.SearchTextChanged("")) }) {
                                    Icon(Icons.Default.Close, contentDescription = stringResource(R.string.clear))
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )
                } else {
                    Text(stringResource(R.string.tmdb_movies))
                }
            },
            actions = {
                if (state.value.showFilterIcon) {
                    IconButton(onClick = {
                        if (isOnlyFavorites) eventHandler(Event.ShowAllClicked) else eventHandler(Event.ShowFavoritesClicked)
                    }) {
                        val icon = if (isOnlyFavorites) Icons.Default.ClearAll else Icons.Default.FilterList
                        val cd =
                            if (isOnlyFavorites) stringResource(R.string.show_all) else stringResource(R.string.only_favourites)
                        Icon(icon, contentDescription = cd)
                    }
                }
                if (state.value.showSearchIcon) {
                    IconButton(onClick = {
                        eventHandler(Event.SearchClicked)
                    }) {
                        Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
                    }
                }
            },
            navigationIcon = {
                if (state.value.showBackButton) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            }
        )
    }
}