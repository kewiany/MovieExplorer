package xyz.kewiany.movieexplorer.common.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class NavGraphTest {

    @Test
    fun movieDetails_destinationFormatsId() {
        val cmd = NavigationDirections.movieDetails(7)
        assertEquals(listOf("movieId"), cmd.arguments.map { it.name })
        assertEquals("movie_details/7", cmd.destination)
    }
}


