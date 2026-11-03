package com.timezen.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class UsageStatusTest {

    @Test
    fun belowEightyPercentIsWithinTarget() {
        assertEquals(UsageStatus.WITHIN_TARGET, UsageStatus.from(usedMinutes = 79, limitMinutes = 100))
    }

    @Test
    fun exactlyEightyPercentIsApproaching() {
        assertEquals(UsageStatus.APPROACHING_LIMIT, UsageStatus.from(usedMinutes = 80, limitMinutes = 100))
    }

    @Test
    fun justBelowLimitIsStillApproaching() {
        assertEquals(UsageStatus.APPROACHING_LIMIT, UsageStatus.from(usedMinutes = 99, limitMinutes = 100))
    }

    @Test
    fun exactlyAtLimitIsReached() {
        assertEquals(UsageStatus.LIMIT_REACHED, UsageStatus.from(usedMinutes = 100, limitMinutes = 100))
    }

    @Test
    fun overLimitIsReached() {
        assertEquals(UsageStatus.LIMIT_REACHED, UsageStatus.from(usedMinutes = 150, limitMinutes = 100))
    }

    @Test
    fun noLimitIsAlwaysWithinTarget() {
        assertEquals(UsageStatus.WITHIN_TARGET, UsageStatus.from(usedMinutes = 500, limitMinutes = 0))
    }
}
