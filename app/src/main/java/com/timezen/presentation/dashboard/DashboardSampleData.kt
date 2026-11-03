package com.timezen.presentation.dashboard

import com.timezen.domain.model.HabitLevel

/**
 * *** SAMPLE DATA - NOT REAL DEVICE USAGE ***
 *
 * These values exist only so the Phase 1 dashboard has something to draw.
 * The app names are invented. This file is deleted from the dashboard flow in Phase 3,
 * when the dashboard reads real numbers from Android's UsageStatsManager.
 */
object DashboardSampleData {

    fun create(): DashboardUiState = DashboardUiState(
        isSampleData = true,
        usedMinutes = 186,
        targetMinutes = 240,
        unlocks = 64,
        notifications = 118,
        averageSessionMinutes = 4,
        appLaunches = 143,
        firstActivity = "06:48",
        lastActivity = "22:15",
        topApps = listOf(
            AppUsageUi(name = "Chatterbox", minutes = 62, opens = 41),
            AppUsageUi(name = "Streamline", minutes = 48, opens = 9),
            AppUsageUi(name = "PixelFeed", minutes = 35, opens = 52),
            AppUsageUi(name = "Notely", minutes = 22, opens = 24),
            AppUsageUi(name = "TrailMap", minutes = 12, opens = 6),
        ),
        weeklyMinutes = listOf(150, 205, 178, 232, 190, 260, 186),
        weekDayLabels = listOf("M", "T", "W", "T", "F", "S", "S"),
        todayIndex = 6,
        productivityScore = 72,
        habitLevel = HabitLevel.BALANCED,
    )
}
