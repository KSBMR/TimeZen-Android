package com.timezen.navigation

/**
 * Every destination (screen) in the app, with the text address ("route") used to open it.
 *
 * A `sealed class` is a closed family of types: only the objects listed here exist.
 * That lets Kotlin warn you if you forget to handle one of them.
 */
sealed class Screen(val route: String, val title: String) {

    // Bottom-navigation destinations
    data object Dashboard : Screen("dashboard", "Home")
    data object Apps : Screen("apps", "Apps")
    data object Focus : Screen("focus", "Focus")
    data object Reports : Screen("reports", "Reports")
    data object Settings : Screen("settings", "Settings")

    // Secondary destinations (opened from the dashboard, no bottom-bar tab)
    data object Limits : Screen("limits", "App limits")
    data object Blocker : Screen("blocker", "Reels & Shorts blocker")
    data object Challenges : Screen("challenges", "Challenges")
}
