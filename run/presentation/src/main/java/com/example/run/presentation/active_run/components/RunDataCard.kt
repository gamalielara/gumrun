package com.example.run.presentation.active_run.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.presentation.designsystem.GumrunTheme
import com.example.presentation.ui.R
import com.example.presentation.ui.formatted
import com.example.presentation.ui.toFormattedKilometers
import com.example.presentation.ui.toFormattedPace
import com.example.run.domain.Run
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

const val METER_TO_KM = 1000.0

@Composable
fun RunDataCard(
    elapsedTime: Duration,
    runData: Run,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RunDataItem(
            title = stringResource(R.string.duration),
            value = elapsedTime.formatted(),
            valueFontSize = 32.sp,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            RunDataItem(
                title = stringResource(R.string.distance),
                value = (runData.distanceMeters / METER_TO_KM).toFormattedKilometers(),
                modifier = Modifier.defaultMinSize(minWidth = 75.dp)
            )

            RunDataItem(
                title = stringResource(R.string.pace),
                value = elapsedTime.toFormattedPace(
                    distanceKm = runData.distanceMeters / METER_TO_KM,
                ),
                modifier = Modifier.defaultMinSize(minWidth = 75.dp)
            )
        }
    }

}

@Composable
private fun RunDataItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueFontSize: TextUnit = 16.sp,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = title, color = MaterialTheme.colorScheme.surfaceVariant, fontSize = 12.sp)
        Text(text = value, color = MaterialTheme.colorScheme.onSurface, fontSize = valueFontSize)
    }
}

@Preview
@Composable
fun RunDataCardPreview() {
    GumrunTheme {
        RunDataCard(
            elapsedTime = 10.minutes,
            runData = Run(
                distanceMeters = 10,
                pace = 6.minutes,
            )
        )
    }
}