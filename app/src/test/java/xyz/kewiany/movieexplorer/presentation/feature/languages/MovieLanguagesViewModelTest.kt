package xyz.kewiany.movieexplorer.presentation.feature.languages

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import xyz.kewiany.menusy.test.common.BaseTest
import xyz.kewiany.movieexplorer.common.Result
import xyz.kewiany.movieexplorer.common.navigation.Navigator
import xyz.kewiany.movieexplorer.domain.usecase.FetchSpokenLanguagesUseCase

class MovieLanguagesViewModelTest : BaseTest() {

    @Test
    fun when_Load_success_then_listIsSet() = runTest {
        val fetch = mockk<FetchSpokenLanguagesUseCase> {
            coEvery { this@mockk.invoke(1) } returns Result.Success(listOf("English", "Polish"))
        }
        val vm = MovieLanguagesViewModel(Navigator(), fetch)

        vm.eventHandler(MovieLanguagesViewModel.Event.Load(1))
        runCurrent()

        Assert.assertEquals(listOf("English", "Polish"), vm.state.value.list)
    }

    @Test
    fun when_Load_error_then_listEmpty() = runTest {
        val fetch = mockk<FetchSpokenLanguagesUseCase> {
            coEvery { this@mockk.invoke(2) } returns Result.Error(Exception("boom"))
        }
        val vm = MovieLanguagesViewModel(Navigator(), fetch)

        vm.eventHandler(MovieLanguagesViewModel.Event.Load(2))
        runCurrent()

        Assert.assertEquals(emptyList<String>(), vm.state.value.list)
    }

    @Test
    fun when_Close_or_Outside_clicked_then_navigatesBack() = runTest {
        val navigator = Navigator()
        val fetch = mockk<FetchSpokenLanguagesUseCase>(relaxed = true)
        val vm = MovieLanguagesViewModel(navigator, fetch)

        var backCount = 0
        val job = launch { navigator.back.collect { backCount++ } }
        runCurrent()
        vm.eventHandler(MovieLanguagesViewModel.Event.CloseClicked)
        runCurrent()
        vm.eventHandler(MovieLanguagesViewModel.Event.OutsideClicked)
        runCurrent()

        job.cancel()
        Assert.assertEquals(2, backCount)
    }
}