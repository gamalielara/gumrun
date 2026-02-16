package com.example.run.presentation.active_run.maps

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.core.domain.util.location.LocationTimestamp
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import kotlin.collections.map

@Composable
fun GumrunPolylines(
    locations: List<List<LocationTimestamp>>
) {

    val polylines = remember(locations) {
        locations.map { loc ->
            loc.zipWithNext { t1, t2 ->
                PolylineUI(
                    loc1 = t1.location.location,
                    loc2 = t2.location.location,
                    color = PolylineColorCalculator.locationToColor(t1, t2)
                )
            }

        }
    }

    polylines.forEach { polyline ->
        polyline.forEach { polylineUI ->
            Log.d("haha", polyline.toString())
            Polyline(
                points = listOf(
                    LatLng(polylineUI.loc1.lat, polylineUI.loc1.long),
                    LatLng(polylineUI.loc2.lat, polylineUI.loc2.long),
                ),
                color = polylineUI.color,
                jointType = JointType.BEVEL
            )

        }
    }
}