package com.timezen.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.timezen.domain.model.ThemeMode

/** A tab in the bottom navigation bar. */
data class BottomNavItem(val screen: Screen, val icon: ImageVector)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Dashboard, Icons.Rounded.Home),
    BottomNavItem(Screen.Apps, Icons.Rounded.Apps),
    BottomNavItem(Screen.Focus, Icons.Rounded.Timer),
    BottomNavItem(Screen.Reports, Icons.Rounded.BarChart),
    BottomNavItem(Screen.Settings, Icons.Rounded.Settings),
)

/** The root composable: bottom bar + the screen for the current route. */
@Composable
fun TimeZenApp(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    // The bottom bar is hidden on secondary screens (they have their own back arrow).
    val showBottomBar = bottomNavItems.any { item -> currentDestination.isIn(item.screen) }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                TimeZenBottomBar(currentDestination = currentDestination, navController = navController)
            }
        },
    ) { innerPadding ->
        // innerPadding already accounts for the status bar, the bottom bar and the gesture bar.
        TimeZenNavHost(
            navController = navController,
            themeMode = themeMode,
            onThemeModeChange = onThemeModeChange,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun TimeZenBottomBar(
    currentDestination: NavDestination?,
    navController: NavController,
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination.isIn(item.screen),
                onClick = { navController.navigateToTopLevel(item.screen.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = null) },
                label = { Text(item.screen.title) },
            )
        }
    }
}

private fun NavDestination?.isIn(screen: Screen): Boolean =
    this?.hierarchy?.any { it.route == screen.route } == true

/**
 * Switch between bottom-bar tabs the standard way: keep one copy of each tab on the
 * back stack and remember its scroll position and state.
 */
private fun NavController.navigateToTopLevel(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
