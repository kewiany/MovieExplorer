package xyz.kewiany.movieexplorer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import xyz.kewiany.movieexplorer.common.navigation.NavGraph
import xyz.kewiany.movieexplorer.common.navigation.NavigationDirections
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.ui.MovieTopBar

@Composable
fun App(navigator: Navigator) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val state = mainViewModel.state.collectAsState()

    val startDestination = NavigationDirections.popularMovies.destination

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    mainViewModel.handleEvent(MainViewModel.Event.SetCurrentRoute(currentRoute))

    val isPopularRoute = currentRoute == NavigationDirections.popularMovies.destination

    LaunchedEffect(navigator.commands) {
        navigator.commands.onEach { command ->
            val destination = command.destination
            navController.navigate(destination)
        }.launchIn(this)
    }

    LaunchedEffect(navigator.back) {
        navigator.back.onEach {
            navController.popBackStack()
        }.launchIn(this)
    }

    Scaffold(
        topBar = {
            MovieTopBar(
                navController = navController,
                state = state,
                eventHandler = mainViewModel::handleEvent,
                isOnlyFavorites = !isPopularRoute
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding())
            ) {
                NavGraph(
                    navController = navController,
                    startDestination = startDestination,
                )
            }
        }
    )
}