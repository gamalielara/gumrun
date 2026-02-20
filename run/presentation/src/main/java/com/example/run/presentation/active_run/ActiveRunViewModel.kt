package com.example.run.presentation.active_run

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.run.domain.RunningTracker
import com.example.run.presentation.active_run.service.ActiveRunService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class ActiveRunViewModel(
    private val runningTracker: RunningTracker
) : ViewModel() {
    var state by mutableStateOf(
        ActiveRunState(
            shouldTrack = ActiveRunService.isServiceActive && runningTracker.isTracking.value,
            hasStartedRunning = ActiveRunService.isServiceActive
        )
    )
        private set

    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    // stateIn converts the state into Flow
    // stateIn converts the flow into StateFlow
    private val shouldTrack = snapshotFlow { state.shouldTrack }.stateIn(
        viewModelScope, SharingStarted.Lazily, state.shouldTrack
    )

    private val _hasLocationPerm = MutableStateFlow(false)

    private val isTracking = combine(
        shouldTrack, _hasLocationPerm
    ) { shouldTrack, hasPerm ->
        shouldTrack && hasPerm
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        _hasLocationPerm.onEach { hasPermission ->
            if (hasPermission) {
                runningTracker.startObservingLocation()
            } else {
                runningTracker.stopObservingLocation()
            }
        }.launchIn(viewModelScope)


        isTracking.onEach { isTracking ->
            runningTracker.setIsTracking(isTracking)
        }.launchIn(viewModelScope)

        runningTracker.currentLocationFlow.onEach {
            state = state.copy(currentLocation = it?.location)
        }.launchIn(viewModelScope)

        runningTracker.runData.onEach { state = state.copy(runData = it) }.launchIn(viewModelScope)

        runningTracker.elapsedTime.onEach { state = state.copy(elapsedTime = it) }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ActiveRunAction) {
        when (action) {
            ActiveRunAction.OnBackClick -> {
                state = state.copy(shouldTrack = false)
            }

            ActiveRunAction.OnFinishRunClick -> {}

            ActiveRunAction.OnResumeRunClick -> {
                state = state.copy(shouldTrack = true)
            }

            ActiveRunAction.OnToggleRunClick -> {
                state = state.copy(
                    hasStartedRunning = true,
                    shouldTrack = !state.shouldTrack
                )
            }

            ActiveRunAction.DismissRationaleDialog -> {
                state = state.copy(
                    showLocationRationale = false, showNotificationRationale = false
                )
            }

            is ActiveRunAction.SubmitLocationInfo -> {
                _hasLocationPerm.value = action.acceptedLocationPermission

                state = state.copy(
                    showLocationRationale = action.showLocationRationale
                )
            }

            is ActiveRunAction.SubmitNotificationPermissionInfo -> {
                state = state.copy(
                    showNotificationRationale = action.showNotiRationale
                )
            }
        }
    }

    override fun onCleared() {
        // When the view model is destroyed (i.e. the app is cleared)
        super.onCleared()

        if(!ActiveRunService.isServiceActive){
            runningTracker.stopObservingLocation()
        }
    }
}