package com.example.run.domain

import com.example.core.domain.util.location.LocationTimestamp
import kotlin.time.Duration

data class Run(
    val distanceMeters: Int = 0,
    val pace: Duration = Duration.ZERO,
    val locations: List<List<LocationTimestamp>> = emptyList()
)
