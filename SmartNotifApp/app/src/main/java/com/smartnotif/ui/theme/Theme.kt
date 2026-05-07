package com.smartnotif.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Color palette ─────────────────────────────────────────────────────────
val Purple80   = Color(0xFFD0BCFF)
val Purple40   = Color(0xFF7F77DD)
val Purple20   = Color(0xFF534AB7)
val Teal40     = Color(0xFF1D9E75)
val Coral40    = Color(0xFFD85A30)
val Amber40    = Color(0xFFBA7517)
val Green40    = Color(0xFF639922)

private val DarkColors = darkColorScheme(
    primary          = Purple80,
    onPrimary        = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    secondary        = Color(0xFFCCC2DC),
    surface          = Color(0xFF1C1B1F),
    background       = Color(0xFF1C1B1F),
)

private val LightColors = lightColorScheme(
    primary          = Purple40,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFFEEEDFE),
    secondary        = Color(0xFF625B71),
    surface          = Color.White,
    background       = Color(0xFFF7F6FF),
)

@Composable
fun SmartNotifTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography  = Typography(),
        content     = content
    )
}
