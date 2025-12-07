package com.example.core.presentation.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = GumrunGreen,
    background = GumrunBlack,
    surface = GumrunDarkGray,
    secondary = GumrunWhite,
    tertiary = GumrunWhite,
    primaryContainer = GumrunGreen30,
    onPrimary = GumrunBlack,
    onBackground = GumrunWhite,
    onSurface = GumrunWhite,
    onSurfaceVariant = GumrunGray,
    error = GumrunDarkRed
)

@Composable
fun GumrunTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
//    val view = LocalView.current
//
//    if (!view.isInEditMode) {
//
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
//
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
        typography = AppTypography
    )
}