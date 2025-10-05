package com.linkjf.spacex_launch.home.data.repository

import com.linkjf.spacex_launch.home.data.TestFixtures
import com.linkjf.spacex_launch.home.data.remote.SpaceXApi
import com.linkjf.spacex_launch.home.domain.model.Launch
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class LaunchRepositoryImplTest {

    private lateinit var spaceXApi: SpaceXApi
    private lateinit var repository: LaunchRepositoryImpl

    @Before
    fun setup() {
        spaceXApi = mockk()
        repository = LaunchRepositoryImpl(spaceXApi)
    }

    @Test
    fun `getUpcomingLaunches should return success when API call succeeds`() = runTest {
        val mockLaunches = TestFixtures.createMockLaunchDtos()
        coEvery { spaceXApi.getUpcomingLaunches() } returns mockLaunches

        val result = repository.getUpcomingLaunches()

        assertTrue(result.isSuccess)
        assertEquals(mockLaunches.size, result.getOrNull()?.size)
        assertEquals("Falcon 9 Test Flight", result.getOrNull()?.first()?.name)
        coVerify { spaceXApi.getUpcomingLaunches() }
    }

    @Test
    fun `getUpcomingLaunches should return failure when API call fails`() = runTest {
        val networkError = IOException("Network error")
        coEvery { spaceXApi.getUpcomingLaunches() } throws networkError

        val result = repository.getUpcomingLaunches()

        assertFalse(result.isSuccess)
        assertEquals(networkError, result.exceptionOrNull())
        coVerify { spaceXApi.getUpcomingLaunches() }
    }

    @Test
    fun `getUpcomingLaunches should map DTOs to domain models correctly`() = runTest {
        val mockLaunches = TestFixtures.createMockLaunchDtos()
        coEvery { spaceXApi.getUpcomingLaunches() } returns mockLaunches

        val result = repository.getUpcomingLaunches()

        assertTrue(result.isSuccess)
        val domainLaunches = result.getOrNull()!!
        assertEquals("launch_1", domainLaunches[0].id)
        assertEquals("Falcon 9 Test Flight", domainLaunches[0].name)
        assertEquals("2024-01-15T14:30:00.000Z", domainLaunches[0].dateUtc)
        assertEquals("falcon9", domainLaunches[0].rocketId)
        assertEquals("ksc_lc_39a", domainLaunches[0].launchpadId)
        assertEquals("Test flight details", domainLaunches[0].details)
        assertEquals(true, domainLaunches[0].success)
        assertEquals(true, domainLaunches[0].upcoming)
        assertEquals("https://example.com/patch_small.png", domainLaunches[0].links?.patch?.small)
        assertEquals("https://example.com/patch_large.png", domainLaunches[0].links?.patch?.large)
        assertEquals("https://youtube.com/watch?v=dQw4w9WgXcQ", domainLaunches[0].links?.webcast)
    }

    @Test
    fun `getUpcomingLaunches should handle empty response`() = runTest {
        coEvery { spaceXApi.getUpcomingLaunches() } returns emptyList()

        val result = repository.getUpcomingLaunches()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getUpcomingLaunches should handle null values in DTO`() = runTest {
        val mockLaunches = TestFixtures.createMockLaunchDtosWithNulls()
        coEvery { spaceXApi.getUpcomingLaunches() } returns mockLaunches

        val result = repository.getUpcomingLaunches()

        assertTrue(result.isSuccess)
        val domainLaunch = result.getOrNull()?.first()!!
        assertEquals("launch_null", domainLaunch.id)
        assertEquals("Test Launch", domainLaunch.name)
        assertEquals("2024-01-15T14:30:00.000Z", domainLaunch.dateUtc)
        assertEquals("falcon9", domainLaunch.rocketId)
        assertEquals("ksc_lc_39a", domainLaunch.launchpadId)
        assertNull(domainLaunch.details)
        assertNull(domainLaunch.success)
        assertEquals(true, domainLaunch.upcoming)
        assertNull(domainLaunch.links?.patch)
        assertNull(domainLaunch.links?.webcast)
    }

    @Test
    fun `getUpcomingLaunches should handle different launch types`() = runTest {
        val mockLaunches = TestFixtures.createMockLaunchDtos()
        coEvery { spaceXApi.getUpcomingLaunches() } returns mockLaunches

        val result = repository.getUpcomingLaunches()

        assertTrue(result.isSuccess)
        val domainLaunches = result.getOrNull()!!
        
        // Test first launch (successful)
        assertEquals(true, domainLaunches[0].success)
        assertEquals(true, domainLaunches[0].upcoming)
        assertEquals("https://youtube.com/watch?v=dQw4w9WgXcQ", domainLaunches[0].links?.webcast)
        
        // Test second launch (null success, upcoming)
        assertNull(domainLaunches[1].success)
        assertEquals(true, domainLaunches[1].upcoming)
        assertNull(domainLaunches[1].links?.webcast)
        
        // Test third launch (failed, not upcoming)
        assertEquals(false, domainLaunches[2].success)
        assertEquals(false, domainLaunches[2].upcoming)
        assertEquals("https://youtube.com/watch?v=Lk4zQ2wP-Nc", domainLaunches[2].links?.webcast)
    }

    @Test
    fun `getUpcomingLaunches should handle API exception`() = runTest {
        val apiException = RuntimeException("API service unavailable")
        coEvery { spaceXApi.getUpcomingLaunches() } throws apiException

        val result = repository.getUpcomingLaunches()

        assertFalse(result.isSuccess)
        assertEquals(apiException, result.exceptionOrNull())
        coVerify { spaceXApi.getUpcomingLaunches() }
    }
}
