package com.example.core.presentation.designsystem.components

import android.R.attr.content
import android.R.id.content
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GumrunScaffold(
    withGradient: Boolean = true,
    modifier: Modifier = Modifier,
    topAppBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (paddingValue: PaddingValues) -> Unit = {}
) {
    Scaffold(
        topBar = topAppBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.Center,
        modifier = modifier,
    ) { paddingValue ->
        if (withGradient) {
            GradientBackground {
                content(paddingValue)
            }
        } else {
            content(paddingValue)
        }
    }
}