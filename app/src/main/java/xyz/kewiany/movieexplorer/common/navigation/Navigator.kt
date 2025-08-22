package xyz.kewiany.movieexplorer.common.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {

    private val _back = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val back = _back.asSharedFlow()

    private val _commands = MutableSharedFlow<NavigationCommand>(extraBufferCapacity = 1)
    val commands = _commands.asSharedFlow()

    fun back() {
        _back.tryEmit(Unit)
    }

    fun navigate(directions: NavigationCommand) {
        _commands.tryEmit(directions)
    }
}
