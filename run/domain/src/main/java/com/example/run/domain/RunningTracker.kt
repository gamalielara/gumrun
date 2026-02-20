package com.example.run.domain

import com.example.core.domain.util.location.LocationTimestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalCoroutinesApi::class)
class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {
    private val _runData = MutableStateFlow(Run())
    val runData = _runData.asStateFlow()

    private val isObservingLocation = MutableStateFlow<Boolean>(false)
    private val _isTracking = MutableStateFlow(false)
    val isTracking = _isTracking.asStateFlow()

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime = _elapsedTime.asStateFlow()


    // Will be triggered whenever the flow's value changes
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

    init {
        _isTracking.onEach { isTracking ->
            if (!isTracking) {
                val newList = buildList {
                    addAll(runData.value.locations)
                    add(emptyList<LocationTimestamp>())
                }.toList()

                _runData.update {
                    it.copy(
                        locations = newList
                    )
                }

            }

        }.flatMapLatest { isTracking ->
            if (isTracking) {
                Timer.timeAndAEmit()
            } else {
                // Emit empty flow
                flowOf()
            }
        }.onEach {
            _elapsedTime.value += it
        }.launchIn(applicationScope)

        // Combine locstion flow with isTracking flow
        currentLocationFlow
            .filterNotNull()
            .combineTransform(_isTracking) { location, isTracking ->
                if (isTracking) {
                    emit(location)
                }
            }
            .zip(_elapsedTime) { location, elapsedTime ->
                LocationTimestamp(
                    location = location,
                    durationTimestamp = elapsedTime
                )

            }
            .onEach { locationWithTimestamp ->
                val currLocation = runData.value.locations
                val lastLoc = if (currLocation.isNotEmpty()) {
                    currLocation.last() + locationWithTimestamp
                } else {
                    listOf(locationWithTimestamp)
                }

                val newLoc = currLocation.replaceLast(lastLoc)
                val distanceMeters = LocationDataCalculator.getTotalDistanceMeters(newLoc)

                val distanceInKM = distanceMeters / 1000.0
                val duration = locationWithTimestamp.durationTimestamp

                val avgSecPerKM =
                    if (distanceInKM == 0.0) 0 else (duration.inWholeSeconds / distanceInKM).roundToInt()

                println("newloc $newLoc")
                _runData.update {
                    Run(
                        distanceMeters = distanceMeters,
                        pace = avgSecPerKM.seconds,
                        locations = newLoc
                    )
                }
            }
            .launchIn(applicationScope)
    }

    fun setIsTracking(isTracking: Boolean) {
        this._isTracking.value = isTracking
    }

    fun startObservingLocation() {
        isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
    }

    private fun <T> List<List<T>>.replaceLast(replacement: List<T>): List<List<T>> {
        if (isEmpty()) {
            return listOf(replacement)
        }

        return this.dropLast(1) + listOf(replacement)
    }
}