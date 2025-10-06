package com.linkjf.spacex.launch.home.presentation

import androidx.paging.PagingData
import app.cash.turbine.test
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetUpcomingLaunchesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val mockGetUpcomingLaunches = mockk<GetUpcomingLaunchesUseCase>(relaxed = true)
    private val mockGetPastLaunches = mockk<GetPastLaunchesUseCase>(relaxed = true)

    private fun createViewModel(): HomeViewModel {
        every { mockGetUpcomingLaunches() } returns flowOf(PagingData.empty())
        every { mockGetPastLaunches() } returns flowOf(PagingData.empty())
        return HomeViewModel(mockGetUpcomingLaunches, mockGetPastLaunches)
    }

    @Test
    fun `initial state should have correct default values`() =
        runTest {
            // When
            val viewModel = createViewModel()

            // Then
            assertEquals(LaunchFilter.UPCOMING, viewModel.state.value.selectedFilter)
            assertEquals(0, viewModel.state.value.selectedTabIndex)
            assertEquals(null, viewModel.state.value.errorMessage)
            assertEquals(null, viewModel.state.value.rateLimitError)
        }

    @Test
    fun `TapSettings should emit NavigateToSettings event`() =
        runTest {
            // Given
            val viewModel = createViewModel()

            // When
            viewModel.event.test {
                viewModel.reduce(HomeAction.TapSettings)
                assertEquals(HomeEvent.NavigateToSettings, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SelectFilter should update state when filter changes`() =
        runTest {
            // Given
            val viewModel = createViewModel()

            // When
            viewModel.reduce(HomeAction.SelectFilter(LaunchFilter.PACK))

            // Then
            assertEquals(LaunchFilter.PACK, viewModel.state.value.selectedFilter)
            assertEquals(1, viewModel.state.value.selectedTabIndex)
        }

    @Test
    fun `SelectFilter should not update state when filter is same`() =
        runTest {
            // Given
            val viewModel = createViewModel()

            // When
            viewModel.state.test {
                assertEquals(LaunchFilter.UPCOMING, awaitItem().selectedFilter)
                viewModel.reduce(HomeAction.SelectFilter(LaunchFilter.UPCOMING))
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SelectTab should update filter based on tab index`() =
        runTest {
            // Given
            val viewModel = createViewModel()

            // When
            viewModel.reduce(HomeAction.SelectTab(1))

            // Then
            assertEquals(LaunchFilter.PACK, viewModel.state.value.selectedFilter)
            assertEquals(1, viewModel.state.value.selectedTabIndex)
        }

    @Test
    fun `TapLaunch should emit NavigateToLaunchDetails event`() =
        runTest {
            // Given
            val viewModel = createViewModel()
            val launchId = "test-launch-id"

            // When
            viewModel.event.test {
                viewModel.reduce(HomeAction.TapLaunch(launchId))
                assertEquals(HomeEvent.NavigateToLaunchDetails(launchId), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `DismissError should clear error message`() =
        runTest {
            // Given
            val viewModel = createViewModel()

            // When
            viewModel.reduce(HomeAction.DismissError)

            // Then
            assertEquals(null, viewModel.state.value.errorMessage)
        }

    @Test
    fun `DismissRateLimitError should clear rate limit error`() =
        runTest {
            // Given
            val viewModel = createViewModel()

            // When
            viewModel.reduce(HomeAction.DismissRateLimitError)

            // Then
            assertEquals(null, viewModel.state.value.rateLimitError)
        }

    @Test
    fun `upcomingLaunches should return flow from use case`() =
        runTest {
            // Given
            every { mockGetUpcomingLaunches() } returns flowOf(PagingData.empty())

            // When
            val viewModel = HomeViewModel(mockGetUpcomingLaunches, mockGetPastLaunches)

            // Then
            viewModel.upcomingLaunches.test {
                awaitItem()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `pastLaunches should return flow from use case`() =
        runTest {
            // Given
            every { mockGetPastLaunches() } returns flowOf(PagingData.empty())

            // When
            val viewModel = HomeViewModel(mockGetUpcomingLaunches, mockGetPastLaunches)

            // Then
            viewModel.pastLaunches.test {
                awaitItem()
                cancelAndIgnoreRemainingEvents()
            }
        }
}
