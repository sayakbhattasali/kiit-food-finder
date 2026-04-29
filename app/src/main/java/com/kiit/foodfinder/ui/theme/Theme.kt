package com.kiit.foodfinder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────
//  Core palette
// ─────────────────────────────────────────
val Surface900 = Color(0xFF0F1115) // Deep Charcoal (not pure black)
val Surface800 = Color(0xFF16191E) // Slightly lighter for cards
val Surface700 = Color(0xFF1E2228) // Elevation
val Surface600 = Color(0xFF2A3038) // Borders

val TextPrimary   = Color(0xFFF2F2F5)
val TextSecondary = Color(0xFF8888A0)
val TextTertiary  = Color(0xFF4A4A60)

// ─────────────────────────────────────────
//  Brand — Greenish & Cyan identity (Premium, not too neon)
// ─────────────────────────────────────────
val BrandPrimary   = Color(0xFF1DB954)   // Spotify Green — primary CTA
val BrandSecondary = Color(0xFF00ACC1)   // Subdued Cyan — accents / tags
val TealAccent     = Color(0xFF00BFA5)   // Vibrant Teal — highlights
val BrandGlow      = Color(0xFF1ED760)   // Vibrant Green for glows
val BrandDim       = Color(0xFF0F2618)   // Very dark forest for subtle fills

val BrandGradient: Brush = Brush.linearGradient(
    colors = listOf(BrandPrimary, BrandSecondary)
)

// ─────────────────────────────────────────
//  Semantic
// ─────────────────────────────────────────
val GreenOpen  = Color(0xFF00C853)
val RedClosed  = Color(0xFFFF5252)
val StarYellow = Color(0xFFFFD600)

// ─────────────────────────────────────────
//  Material 3 scheme
// ─────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary          = BrandPrimary,
    onPrimary        = Color.White,
    primaryContainer = BrandDim,
    secondary        = BrandSecondary,
    onSecondary      = Color.White,
    background       = Surface900,
    onBackground     = TextPrimary,
    surface          = Surface800,
    onSurface        = TextPrimary,
    surfaceVariant   = Surface700,
    onSurfaceVariant = TextSecondary,
    outline          = Surface600,
    error            = RedClosed,
    onError          = Color.White
)

@Composable
fun KIITFoodFinderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
