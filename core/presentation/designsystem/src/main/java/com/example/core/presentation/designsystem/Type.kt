package com.example.core.presentation.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppFont = FontFamily(
    Font(
        resId = R.font.zalando_sans_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.zalando_sans_semi_bold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.zalando_sans_regular,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.zalando_sans_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.zalando_sans_bold,
        weight = FontWeight.Bold
    )
)

val AppTypography = Typography(
    bodySmall = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        color = GumrunGray
    ),
    bodyMedium = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = .5.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        color = GumrunWhite
    ),
)