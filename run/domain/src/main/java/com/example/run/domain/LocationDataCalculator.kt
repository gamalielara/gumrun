package com.example.run.domain

import com.example.core.domain.util.location.LocationTimestamp
import kotlin.math.roundToInt

object LocationDataCalculator {
    fun getTotalDistanceMeters(locations: List<List<LocationTimestamp>>): Int {
        return locations
            .sumOf { timestampPerLine ->
                timestampPerLine.zipWithNext { loc1, loc2 ->
                    loc1.location.location.distanceTo(loc2.location.location)
                }.sum().roundToInt()
            }
    }
}