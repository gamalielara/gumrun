package com.example.run.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {
    private val isObservingLocation = MutableStateFlow<Boolean>(false)

    // Will be triggered whenever the flow's value changes
    // FlatMapLatest -> map the outcome of the boolean flow to a different flow
    val currentLocationFlow = isObservingLocation.flatMapLatest { isObserving ->
        if (isObserving) {
            locationObserver.observeLocation(1000L)
        } else {
            flowOf()
        }
    }.stateIn(
        applicationScope,
        SharingStarted.Lazily,
        null
    )

    fun startObservingLocation() {
        isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
    }
}