package com.timezen.presentation.dashboard

import com.timezen.domain.model.HabitLevel
import com.timezen.domain.model.UsageStatus

/** One application in the "Most used apps" list. */
data class AppUsageUi(
    val name: String,
    val minutes: Int,
    val opens: Int,
)

/**
 * Everything the dashboard screen needs to draw itself.
 *
 * A `data class` is Kotlin's way to hold related values together. Kotlin generates
 * equals(), toString() and copy() for it automatically.
 *
 * `isSampleData` is true while the numbers are placeholders. In Phase 2 the numbers come
 * from Android's UsageStatsManager and this flag becomes false.
 */
data class DashboardUiState(
    val isSampleData: Boolean,
    val usedMinutes: Int,
    val targetMinutes: Int,
    val unlocks: Int,
    val notifications: Int,
    val averageSessionMinutes: Int,
    val appLaunches: Int,
    val firstActivity: String,
    val lastActivity: String,
    val topApps: List<AppUsageUi>,
    val weeklyMinutes: List<Int>,
    val weekDayLabels: List<String>,
    val todayIndex: Int,
    val productivityScore: Int,
    val habitLevel: HabitLevel,
) {
    // "Derived" values are calculated from the fields above, so they can never disagree.
    val remainingMinutes: Int
        get() = (targetMinutes - usedMinutes).coerceAtLeast(0)

    val usagePercent: Int
        get() = if (targetMinutes <= 0) 0 else usedMinutes * 100 / targetMinutes

    val usageFraction: Float
        get() = if (targetMinutes <= 0) 0f else (usedMinutes.toFloat() / targetMinutes).coerceIn(0f, 1f)

    val status: UsageStatus
        get() = UsageStatus.from(usedMinutes, targetMinutes)

    val mostUsedApp: AppUsageUi?
        get() = topApps.maxByOrNull { it.minutes }

    val mostOpenedApp: AppUsageUi?
        get() = topApps.maxByOrNull { it.opens }

    val weeklyAverageMinutes: Int
        get() = if (weeklyMinutes.isEmpty()) 0 else weeklyMinutes.sum() / weeklyMinutes.size

    /** Yesterday's value is the entry before today in the weekly list (0 if there is none). */
    val yesterdayMinutes: Int
        get() = weeklyMinutes.getOrElse(todayIndex - 1) { 0 }

    val differenceFromYesterdayMinutes: Int
        get() = usedMinutes - yesterdayMinutes

    fun shareOfTotalPercent(app: AppUsageUi): Int =
        if (usedMinutes <= 0) 0 else app.minutes * 100 / usedMinutes
}
