package com.eduardozanela.budget.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF05DA93),     // Mint green
    onPrimary = Color.White,
    secondary = Color(0xFF20639B),   // Blue accent
    background = Color(0xFF152032),  // Deep dark
    onBackground = Color.White,             // Text
    surface = Color(0xFF17253E),     // Dark navy
    onSurface = Color.White,

)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF05DA93),     // Mint green
    onPrimary = Color.White,
    secondary = Color(0xFF20639B),   // Blue accent
    background = Color(0xFFE0E3E7),  // Light gray
    onBackground = Color(0xFF17253E),// Navy text
    surface = Color(0xFFECF0F3),     // Card backgrounds
    onSurface = Color(0xFF17253E)
)