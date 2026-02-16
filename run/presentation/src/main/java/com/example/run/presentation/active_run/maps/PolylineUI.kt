package com.example.run.presentation.active_run.maps

import androidx.compose.ui.graphics.Color
import com.example.core.domain.util.location.Location

data class PolylineUI(
    val loc1: Location,
    val loc2: Location,
    val color: Color
)
