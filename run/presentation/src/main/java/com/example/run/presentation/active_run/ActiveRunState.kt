package com.example.run.presentation.active_run

import com.example.core.domain.util.location.Location
import com.example.run.domain.Run
import kotlin.time.Duration

data class ActiveRunState(
    val elapsedTime: Duration = Duration.ZERO,
    val shouldTrack: Boolean = false,
    val hasStartedRunning: Boolean = false,
    val currentLocation: Location? = null,
    val isRunFinished: Boolean = false,
    val isRunSaving: Boolean = false,
    val runData: Run = Run()
)
