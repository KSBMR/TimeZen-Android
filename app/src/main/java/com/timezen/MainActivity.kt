package com.timezen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timezen.navigation.TimeZenApp
import com.timezen.presentation.AppViewModel
import com.timezen.ui.theme.TimeZenTheme

/**
 * The single Activity. An Activity is one "window" of an Android app. A Compose app uses
 * one Activity and draws every screen inside it with composable functions.
 */
class MainActivity : ComponentActivity() {

    // `by viewModels()` creates the ViewModel once and keeps it across rotations.
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Draw behind the status bar and navigation bar (the modern full-screen look).
        enableEdgeToEdge()

        setContent {
            // Whenever the theme changes in the ViewModel, the whole UI redraws with it.
            val themeMode by appViewModel.themeMode.collectAsStateWithLifecycle()

            TimeZenTheme(themeMode = themeMode) {
                TimeZenApp(
                    themeMode = themeMode,
                    onThemeModeChange = appViewModel::setThemeMode,
                )
            }
        }
    }
}
