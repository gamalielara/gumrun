package com.example.run.presentation.active_run

sealed interface ActiveRunAction {
    data object OnToggleRunClick : ActiveRunAction
    data object OnFinishRunClick : ActiveRunAction
    data object OnResumeRunClick : ActiveRunAction
    data object OnBackClick : ActiveRunAction

    data class SubmitLocationInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationRationale: Boolean
    ) : ActiveRunAction

    data class SubmitNotificationPermissionInfo(
        val acceptedNotiPermission: Boolean,
        val showNotiRationale: Boolean
    ) : ActiveRunAction

    data object DismissRationaleDialog: ActiveRunAction

}