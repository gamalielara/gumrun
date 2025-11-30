package com.example.core.presentation.designsystem

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GradientBackground(
    modifier: Modifier,
    hasToolbar: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthInPx = with(density) {
        configuration.screenWidthDp.dp.roundToPx()
    }

    val smallDimension = minOf(
        configuration.screenWidthDp.dp,
        configuration.screenHeightDp.dp
    )

    val smallDimensionInPx = with(density)
    {
        smallDimension.roundToPx()

    }

    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Box(
            modifier = modifier.fillMaxSize().background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryColor,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
        )
    }
}

@Preview
@Composable
private fun GradientBackgroundPreview() {
    GumrunTheme {
        GradientBackground(modifier = Modifier.fillMaxSize()) {}
    }
}