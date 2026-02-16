package com.example.run.presentation.active_run.maps

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.example.core.domain.util.location.LocationTimestamp
import kotlin.math.abs

object PolylineColorCalculator {
    private fun interpolateColor(
        speedKmh: Double,
        minSpeed: Double,
        maxSpeed: Double,
        colorStart: Color,
        colorMid: Color,
        colorEnd: Color
    ): Color {
        val ratio = ((speedKmh - minSpeed) / (maxSpeed - minSpeed)).coerceIn(0.0..1.0)
        val colorInt = if(ratio <= 0.5) {
            val midRatio = ratio / 0.5
            ColorUtils.blendARGB(colorStart.toArgb(), colorMid.toArgb(), midRatio.toFloat())
        } else {
            val midToEndRatio = (ratio - 0.5) / 0.5
            ColorUtils.blendARGB(colorMid.toArgb(), colorEnd.toArgb(), midToEndRatio.toFloat())
        }

        return Color(colorInt)
    }

    fun locationToColor(
        loc1: LocationTimestamp, loc2: LocationTimestamp
    ): Color {
        val distanceMeters = loc1.location.location.distanceTo(loc2.location.location)
        val timeDiff = abs(
            (loc2.durationTimestamp - loc1.durationTimestamp).inWholeSeconds
        )
        val speedInKmH = (distanceMeters / timeDiff) * 3.6

        return interpolateColor(
            speedKmh = speedInKmH,
            minSpeed = 0.0,
            maxSpeed = 20.0,
            colorStart = Color.Green,
            colorMid = Color.Yellow,
            colorEnd = Color.Red
        )
    }
}