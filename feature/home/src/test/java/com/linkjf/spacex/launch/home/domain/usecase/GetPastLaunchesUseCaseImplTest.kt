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
class GetPastLaunchesUseCaseImplTest {
    private val mockRepository = mockk<LaunchRepository>()
    private val useCase = GetPastLaunchesUseCaseImpl(mockRepository)

    @Test
    fun `invoke should return past launches from repository`() =
        runTest {
            // Given
            val expectedLaunches =
                PagingData.from(
                    listOf(
                        LaunchListItem(
                            id = "1",
                            name = "Test Launch 1",
                            date = "2024-01-01",
                            time = "10:00",
                            rocketId = "falcon-9",
                            launchpadId = "slc-40",
                            countdown = "Completed",
                            isUpcoming = false,
                        ),
                        LaunchListItem(
                            id = "2",
                            name = "Test Launch 2",
                            date = "2024-01-02",
                            time = "11:00",
                            rocketId = "falcon-9",
                            launchpadId = "slc-40",
                            countdown = "Completed",
                            isUpcoming = false,
                        ),
                    ),
                )
            every { mockRepository.getPastLaunches() } returns flowOf(expectedLaunches)

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
            every { mockRepository.getPastLaunches() } throws error

            // When & Then
            try {
                useCase.invoke()
            } catch (e: RuntimeException) {
                assertEquals("Repository error", e.message)
            }
        }
}
