package com.garcia.avisoscampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresAvisos = lightColorScheme(
    primary = Color(0xFF196B9A),
    onPrimary = Color.White,
    background = Color(0xFFFAF8FB),
    surface = Color.White
)

@Composable
fun AvisosCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColoresAvisos, content = content)
}
