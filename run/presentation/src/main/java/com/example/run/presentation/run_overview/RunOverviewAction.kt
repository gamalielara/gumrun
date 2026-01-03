package com.example.run.presentation.run_overview

sealed interface RunOverviewAction {
    data object OnStartRun: RunOverviewAction
    data object OnLogoutClick: RunOverviewAction
    data object OnAnalyticsClick: RunOverviewAction
    data object OnSettingsClick: RunOverviewAction
}