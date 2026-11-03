package com.timezen.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.timezen.domain.model.ThemeMode
import com.timezen.presentation.common.ComingSoonScreen
import com.timezen.presentation.dashboard.DashboardScreen
import com.timezen.presentation.settings.SettingsScreen

/**
 * The navigation graph: which composable shows for which route.
 * Screens that are not built yet use [ComingSoonScreen] and say which phase builds them.
 */
@Composable
fun TimeZenNavHost(
    navController: NavHostController,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier,
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(onOpenFeature = { route -> navController.navigate(route) })
        }
        composable(Screen.Apps.route) {
            ComingSoonScreen(
                title = "Apps",
                phaseLabel = "Coming in Phases 2-3",
                description = "Your installed apps, their daily usage, search, sorting and " +
                    "productive / distracting tags will appear here.",
                icon = Icons.Rounded.Apps,
            )
        }
        composable(Screen.Focus.route) {
            ComingSoonScreen(
                title = "Focus Mode",
                phaseLabel = "Coming in Phase 8",
                description = "Focus sessions with a full-screen countdown, app blocking " +
                    "and history will appear here.",
                icon = Icons.Rounded.Timer,
            )
        }
        composable(Screen.Reports.route) {
            ComingSoonScreen(
                title = "Reports",
                phaseLabel = "Coming in Phases 4 and 9",
                description = "Daily, weekly and monthly reports with PDF, CSV and XLSX " +
                    "export will appear here.",
                icon = Icons.Rounded.BarChart,
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(themeMode = themeMode, onThemeModeChange = onThemeModeChange)
        }

        // Secondary screens: reached from the dashboard, with a back arrow.
        composable(Screen.Limits.route) {
            ComingSoonScreen(
                title = Screen.Limits.title,
                phaseLabel = "Coming in Phase 5",
                description = "Daily and weekly limits for each app, with warnings before " +
                    "you reach them.",
                icon = Icons.Rounded.Schedule,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.Blocker.route) {
            ComingSoonScreen(
                title = Screen.Blocker.title,
                phaseLabel = "Coming in Phase 6",
                description = "Block short-form video in supported apps using an Accessibility " +
                    "Service you enable yourself. Detection depends on each app's screen " +
                    "layout and can break when an app updates, so whole-app blocking will be " +
                    "offered as a fallback.",
                icon = Icons.Rounded.VideoLibrary,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.Challenges.route) {
            ComingSoonScreen(
                title = Screen.Challenges.title,
                phaseLabel = "Coming in Phase 10",
                description = "Digital detox challenges, progress tracking and achievement " +
                    "badges based on your real usage.",
                icon = Icons.Rounded.EmojiEvents,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
