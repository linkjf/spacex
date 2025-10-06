package com.linkjf.spacex.launch.home.presentation

import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.LaunchLinks
import com.linkjf.spacex.launch.home.domain.model.LaunchPatch
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetUpcomingLaunchesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
//
//    @get:Rule
//    val mainDispatcherRule = MainDispatcherRule()
//
//    private lateinit var getUpcomingLaunches: GetUpcomingLaunchesUseCase
//    private lateinit var getPastLaunches: GetPastLaunchesUseCase
//    private lateinit var viewModel: HomeViewModel
//
//    private val mockLaunches = listOf(
//        Launch(
//            id = "launch_1",
//            name = "Starlink Group 2-38",
//            dateUtc = "2024-10-04T10:00:00.000Z",
//            rocketId = "falcon9",
//            launchpadId = "ksc_lc_39a",
//            links = LaunchLinks(
//                patch = LaunchPatch(
//                    small = "https://example.com/patch_small.png",
//                    large = "https://example.com/patch_large.png"
//                ),
//                webcast = "https://youtube.com/watch?v=test",
//                youtubeId = "test123"
//            ),
//            details = "Starlink mission",
//            success = null,
//            upcoming = true
//        ),
//        Launch(
//            id = "launch_2",
//            name = "Falcon Heavy Demo",
//            dateUtc = "2024-10-05T16:00:00.000Z",
//            rocketId = "falconheavy",
//            launchpadId = "ksc_lc_39a",
//            links = LaunchLinks(
//                patch = LaunchPatch(
//                    small = "https://example.com/falcon_heavy_patch.png",
//                    large = "https://example.com/falcon_heavy_patch_large.png"
//                ),
//                webcast = null,
//                youtubeId = "heavy123"
//            ),
//            details = "Falcon Heavy demonstration",
//            success = true,
//            upcoming = false
//        )
//    )
//
//    @Before
//    fun setup() {
//        getUpcomingLaunches = mockk()
//        getPastLaunches = mockk()
//        viewModel = HomeViewModel(getUpcomingLaunches, getPastLaunches, mockk())
//    }
//
//    @Test
//    fun `initial state should have correct default values`() = runTest {
//        val initialState = viewModel.state.value
//
//        assertFalse(initialState.isLoading)
//        assertFalse(initialState.isRefreshing)
//        assertFalse(initialState.isLoadingMore)
//        assertEquals(LaunchFilter.UPCOMING, initialState.selectedFilter)
//        assertTrue(initialState.launches.isEmpty())
//        assertNull(initialState.errorMessage)
//    }
//
//    @Test
//    fun `selectFilter should update filter and load launches`() = runTest {
//        coEvery { getPastLaunches() } returns Result.success(mockLaunches)
//
//        viewModel.reduce(HomeAction.SelectFilter(LaunchFilter.PACK))
//
//        assertEquals(LaunchFilter.PACK, viewModel.state.value.selectedFilter)
//        assertTrue(viewModel.state.value.isLoading)
//    }
//
//    @Test
//    fun `pullToRefresh should trigger refresh`() = runTest {
//        coEvery { getUpcomingLaunches() } returns Result.success(mockLaunches)
//
//        viewModel.reduce(HomeAction.PullToRefresh)
//
//        assertTrue(viewModel.state.value.isRefreshing)
//    }
//
//    @Test
//    fun `tapLaunch should emit NavigateToLaunchDetails event`() = runTest {
//        viewModel.event.test {
//            viewModel.reduce(HomeAction.TapLaunch("launch_1"))
//            val event = awaitItem()
//            assertTrue(event is HomeEvent.NavigateToLaunchDetails)
//            assertEquals("launch_1", (event as HomeEvent.NavigateToLaunchDetails).launchId)
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
//
//    @Test
//    fun `tapWatch with webcast should emit OpenWebcast event`() = runTest {
//        // Set up state with launches
//        viewModel.state.value.copy(launches = mockLaunches).sendToState()
//
//        viewModel.event.test {
//            viewModel.reduce(HomeAction.TapWatch("launch_1"))
//            val event = awaitItem()
//            assertTrue(event is HomeEvent.OpenWebcast)
//            assertEquals("https://youtube.com/watch?v=test", (event as HomeEvent.OpenWebcast).url)
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
//
//    @Test
//    fun `tapWatch with youtubeId should emit OpenWebcast event with youtube URL`() = runTest {
//        // Set up state with launches
//        viewModel.state.value.copy(launches = mockLaunches).sendToState()
//
//        viewModel.event.test {
//            viewModel.reduce(HomeAction.TapWatch("launch_2"))
//            val event = awaitItem()
//            assertTrue(event is HomeEvent.OpenWebcast)
//            assertEquals("https://youtube.com/watch?v=heavy123", (event as HomeEvent.OpenWebcast).url)
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
//
//    @Test
//    fun `tapWatch without webcast should emit ShowError event`() = runTest {
//        val launchWithoutWebcast = mockLaunches[0].copy(links = null)
//        viewModel.state.value.copy(launches = listOf(launchWithoutWebcast)).sendToState()
//
//        viewModel.event.test {
//            viewModel.reduce(HomeAction.TapWatch("launch_1"))
//            val event = awaitItem()
//            assertTrue(event is HomeEvent.ShowError)
//            assertEquals("No webcast available for this launch", (event as HomeEvent.ShowError).message)
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
//
//
//    @Test
//    fun `dismissError should clear error message`() = runTest {
//        viewModel.state.value.copy(errorMessage = "Test error").sendToState()
//
//        viewModel.reduce(HomeAction.DismissError)
//
//        assertNull(viewModel.state.value.errorMessage)
//    }
//
//    @Test
//    fun `retry should dismiss error and reload data`() = runTest {
//        coEvery { getUpcomingLaunches() } returns Result.success(mockLaunches)
//
//        viewModel.state.value.copy(errorMessage = "Test error").sendToState()
//
//        viewModel.reduce(HomeAction.Retry)
//
//        assertNull(viewModel.state.value.errorMessage)
//        assertTrue(viewModel.state.value.isLoading)
//    }
//
//    @Test
//    fun `loadLaunches should handle success`() = runTest {
//        coEvery { getUpcomingLaunches() } returns Result.success(mockLaunches)
//
//        viewModel.reduce(HomeAction.SelectFilter(LaunchFilter.UPCOMING))
//
//        // Wait for loading to complete
//        kotlinx.coroutines.delay(100)
//
//        assertEquals(mockLaunches, viewModel.state.value.launches)
//        assertFalse(viewModel.state.value.isLoading)
//        assertNull(viewModel.state.value.errorMessage)
//    }
//
//    @Test
//    fun `loadLaunches should handle failure`() = runTest {
//        val error = IOException("Network error")
//        coEvery { getUpcomingLaunches() } returns Result.failure(error)
//
//        viewModel.reduce(HomeAction.SelectFilter(LaunchFilter.UPCOMING))
//
//        // Wait for loading to complete
//        kotlinx.coroutines.delay(100)
//
//        assertFalse(viewModel.state.value.isLoading)
//        assertEquals("Network error", viewModel.state.value.errorMessage)
//    }
}
