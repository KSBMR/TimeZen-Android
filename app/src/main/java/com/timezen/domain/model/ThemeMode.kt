package com.timezen.domain.model

/** Which color theme the app uses. TimeZen is dark-first, so DARK is the default. */
enum class ThemeMode(val displayName: String) {
    DARK("Dark"),
    LIGHT("Light"),
    SYSTEM("Match my device"),
}
