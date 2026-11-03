package com.timezen.presentation.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Holds the dashboard's state and survives screen rotation.
 *
 * MVVM in one sentence: the ViewModel owns the data, the screen (composable) only
 * displays it and sends user actions back to the ViewModel.
 *
 * `StateFlow` is an observable value holder: the screen "collects" it and redraws
 * automatically whenever the value changes.
 *
 * Phase 1: the state is SAMPLE data. Phase 2 and 3 replace it with a repository that
 * reads real usage statistics, and a use case that builds this state.
 */
class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardSampleData.create())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
}
