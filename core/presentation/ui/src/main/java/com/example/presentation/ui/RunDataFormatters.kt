package com.example.presentation.ui

import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration

const val HOUR_TO_SECOND = 3600
const val MINUTE_TO_SECOND = 60


fun Duration.formatted(): String {
    val totalSeconds = inWholeSeconds
    val hours = String.format("%02d", totalSeconds / HOUR_TO_SECOND)
    val minutes = String.format("%02d", (totalSeconds % HOUR_TO_SECOND) / MINUTE_TO_SECOND)
    val seconds = String.format("%02d", totalSeconds % MINUTE_TO_SECOND)

    return "$hours:$minutes:$seconds"
}

fun Double.toFormattedKilometers(): String = "${this.roundToDecimals(1)} km"

fun Duration.toFormattedPace(distanceKm: Double): String {
    if (this == Duration.ZERO || distanceKm <= 0.0) {
        return "-"
    }

    val secondsPerKm = (this.inWholeSeconds / distanceKm).roundToInt()
    val averagePaceInMinutes = secondsPerKm / MINUTE_TO_SECOND
    // Remaining seconds
    val averagePaceSeconds = String.format("%02d", secondsPerKm % MINUTE_TO_SECOND)

    return "$averagePaceInMinutes:$averagePaceSeconds / km"
}


private fun Double.roundToDecimals(decimalCount: Int): Double {
    val factor = 10f.pow(decimalCount)

    return round(this * factor) / factor
}