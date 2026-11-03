package com.timezen.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.timezen.domain.model.ThemeMode
import com.timezen.domain.model.UsageStatus

private val DarkColors = darkColorScheme(
    primary = TimeZenCyan,
    onPrimary = Color(0xFF00363C),
    primaryContainer = Color(0xFF004F57),
    onPrimaryContainer = Color(0xFFB4F0F7),
    secondary = TimeZenGreen,
    onSecondary = Color(0xFF003919),
    background = TimeZenBackgroundDark,
    onBackground = TimeZenTextPrimaryDark,
    surface = TimeZenCardDark,
    onSurface = TimeZenTextPrimaryDark,
    surfaceVariant = TimeZenTrackDark,
    onSurfaceVariant = TimeZenTextSecondaryDark,
    surfaceContainer = TimeZenCardDark,
    surfaceContainerHigh = TimeZenCardHighDark,
    outline = TimeZenOutlineDark,
    outlineVariant = TimeZenTrackDark,
    error = TimeZenDanger,
    onError = Color.White,
)

private val LightColors = lightColorScheme(
    primary = TimeZenCyanLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB4F0F7),
    onPrimaryContainer = Color(0xFF00363C),
    secondary = TimeZenGreenLight,
    onSecondary = Color.White,
    background = TimeZenBackgroundLight,
    onBackground = TimeZenTextPrimaryLight,
    surface = TimeZenCardLight,
    onSurface = TimeZenTextPrimaryLight,
    surfaceVariant = TimeZenTrackLight,
    onSurfaceVariant = TimeZenTextSecondaryLight,
    surfaceContainer = TimeZenCardLight,
    surfaceContainerHigh = TimeZenTrackLight,
    outline = TimeZenOutlineLight,
    outlineVariant = TimeZenTrackLight,
    error = Color(0xFFB3261E),
    onError = Color.White,
)

/**
 * Wraps the whole app. Every composable inside can read colors, typography and
 * shapes through `MaterialTheme.colorScheme`, `MaterialTheme.typography`, etc.
 */
@Composable
fun TimeZenTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    // Make the status-bar and navigation-bar icons dark or light to match the theme.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = TimeZenTypography,
        content = content,
    )
}

/** Color that represents a usage status: green, orange or red. */
fun UsageStatus.toColor(): Color = when (this) {
    UsageStatus.WITHIN_TARGET -> TimeZenGreen
    UsageStatus.APPROACHING_LIMIT -> TimeZenWarning
    UsageStatus.LIMIT_REACHED -> TimeZenDanger
}

/** Short label shown next to the status color. */
fun UsageStatus.toLabel(): String = when (this) {
    UsageStatus.WITHIN_TARGET -> "Within target"
    UsageStatus.APPROACHING_LIMIT -> "Approaching limit"
    UsageStatus.LIMIT_REACHED -> "Limit reached"
}
