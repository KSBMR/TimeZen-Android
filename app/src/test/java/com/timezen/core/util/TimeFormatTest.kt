package com.timezen.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormatTest {

    @Test
    fun hoursAndMinutes() {
        assertEquals("3h 12m", formatDuration(192))
    }

    @Test
    fun exactHours() {
        assertEquals("3h", formatDuration(180))
    }

    @Test
    fun minutesOnly() {
        assertEquals("45m", formatDuration(45))
    }

    @Test
    fun zero() {
        assertEquals("0m", formatDuration(0))
    }

    @Test
    fun negativeIsTreatedAsZero() {
        assertEquals("0m", formatDuration(-5))
    }
}
