package com.timezen.core.util

/**
 * Turns a number of minutes into short, readable text.
 *
 * Examples: 192 -> "3h 12m", 180 -> "3h", 45 -> "45m", 0 -> "0m".
 * Negative values are treated as 0.
 */
fun formatDuration(totalMinutes: Int): String {
    val safeMinutes = totalMinutes.coerceAtLeast(0)
    val hours = safeMinutes / 60
    val minutes = safeMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}
