package com.timezen.presentation.dashboard

import com.timezen.domain.model.UsageStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Checks the derived values of the dashboard state, using the labeled sample data. */
class DashboardUiStateTest {

    private val state = DashboardSampleData.create()

    @Test
    fun sampleDataIsMarkedAsSample() {
        assertTrue(state.isSampleData)
    }

    @Test
    fun remainingTimeIsTargetMinusUsed() {
        assertEquals(54, state.remainingMinutes) // 240 - 186
    }

    @Test
    fun remainingTimeNeverGoesBelowZero() {
        assertEquals(0, state.copy(usedMinutes = 300).remainingMinutes)
    }

    @Test
    fun usagePercentRoundsDown() {
        assertEquals(77, state.usagePercent) // 186 / 240 = 77.5%
    }

    @Test
    fun statusFollowsPercent() {
        assertEquals(UsageStatus.WITHIN_TARGET, state.status)
        assertEquals(UsageStatus.APPROACHING_LIMIT, state.copy(usedMinutes = 200).status)
        assertEquals(UsageStatus.LIMIT_REACHED, state.copy(usedMinutes = 240).status)
    }

    @Test
    fun zeroTargetDoesNotCrash() {
        val noTarget = state.copy(targetMinutes = 0)
        assertEquals(0, noTarget.usagePercent)
        assertEquals(0f, noTarget.usageFraction, 0.0001f)
    }

    @Test
    fun mostUsedAndMostOpenedAppsAreFoundSeparately() {
        assertEquals("Chatterbox", state.mostUsedApp?.name)
        assertEquals("PixelFeed", state.mostOpenedApp?.name)
    }

    @Test
    fun shareOfTotalUsesTodaysTotal() {
        assertEquals(33, state.shareOfTotalPercent(state.topApps.first())) // 62 / 186
    }

    @Test
    fun weeklyAverageAndYesterdayComparison() {
        assertEquals(200, state.weeklyAverageMinutes) // 1401 / 7, rounded down
        assertEquals(260, state.yesterdayMinutes)
        assertEquals(-74, state.differenceFromYesterdayMinutes) // 186 - 260
    }
}
