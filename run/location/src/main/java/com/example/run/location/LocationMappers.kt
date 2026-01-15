package com.example.run.location

import android.location.Location
import com.example.core.domain.util.location.LocationWithAltitude

fun Location.toLocationWithAltitude(): LocationWithAltitude {
    return LocationWithAltitude(
        location = com.example.core.domain.util.location.Location(
            lat = latitude,
            long = longitude
        ),
        altitude = altitude
    )
}