package com.timezen.presentation

import androidx.lifecycle.ViewModel
import com.timezen.domain.model.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * App-wide state shared by all screens. For now: the chosen theme.
 *
 * Phase 1: the choice is kept in memory only, so it resets when the app process is
 * closed. Phase 11 stores it with Jetpack DataStore so it is remembered.
 */
class AppViewModel : ViewModel() {

    private val _themeMode = MutableStateFlow(ThemeMode.DARK)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }
}
