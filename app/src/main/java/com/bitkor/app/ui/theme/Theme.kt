package com.bitkor.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val DarkColors = darkColorScheme()
val LightColors = lightColorScheme()

@Composable
fun BitkorTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = BitKorShapes,
        typography = BitkorTypography,
        content = content
    )
}