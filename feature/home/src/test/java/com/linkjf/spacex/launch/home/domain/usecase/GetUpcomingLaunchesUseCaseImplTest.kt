package com.linkjf.spacex.launch.home.domain.usecase

import androidx.paging.PagingData
import app.cash.turbine.test
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetUpcomingLaunchesUseCaseImplTest {
    private val mockRepository = mockk<LaunchRepository>()
    private val useCase = GetUpcomingLaunchesUseCaseImpl(mockRepository)

    @Test
    fun `invoke should return upcoming launches from repository`() =
        runTest {
            // Given
            val expectedLaunches =
                PagingData.from(
                    listOf(
                        LaunchListItem(
                            id = "1",
                            name = "Upcoming Launch 1",
                            date = "2024-12-01",
                            time = "10:00",
                            rocketId = "falcon-9",
                            launchpadId = "slc-40",
                            countdown = "2d 5h",
                            isUpcoming = true,
                        ),
                        LaunchListItem(
                            id = "2",
                            name = "Upcoming Launch 2",
                            date = "2024-12-02",
                            time = "11:00",
                            rocketId = "falcon-9",
                            launchpadId = "slc-40",
                            countdown = "1d 3h",
                            isUpcoming = true,
                        ),
                    ),
                )
            every { mockRepository.getUpcomingLaunches() } returns flowOf(expectedLaunches)

            // When
            val result = useCase.invoke()

            // Then
            result.test {
                val actualLaunches = awaitItem()
                assertEquals(expectedLaunches, actualLaunches)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `invoke should propagate repository errors`() =
        runTest {
            // Given
            val error = RuntimeException("Repository error")
            every { mockRepository.getUpcomingLaunches() } throws error

            // When & Then
            try {
                useCase.invoke()
            } catch (e: RuntimeException) {
                assertEquals("Repository error", e.message)
            }
        }
}
