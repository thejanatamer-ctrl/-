package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = IslamicGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = IslamicGreenContainer,
    onPrimaryContainer = OnIslamicGreenContainer,
    secondary = IslamicGoldPrimary,
    onSecondary = Color(0xFF2B2100),
    secondaryContainer = IslamicGoldContainer,
    onSecondaryContainer = OnIslamicGoldContainer,
    tertiary = IslamicGreenMedium,
    onTertiary = Color.White,
    background = IslamicBeigeBackground,
    onBackground = IslamicTextDark,
    surface = IslamicBeigeSurface,
    onSurface = IslamicTextDark,
    surfaceVariant = IslamicBeigeCard,
    onSurfaceVariant = IslamicTextMuted,
    outline = IslamicBeigeBorder,
    outlineVariant = IslamicGoldLight
)

private val DarkColorScheme = darkColorScheme(
    primary = IslamicGreenLight,
    onPrimary = Color.White,
    primaryContainer = DarkGreenCard,
    onPrimaryContainer = IslamicGoldLight,
    secondary = IslamicGoldBright,
    onSecondary = Color(0xFF241A00),
    secondaryContainer = DarkGreenBorder,
    onSecondaryContainer = IslamicGoldLight,
    tertiary = IslamicGoldPrimary,
    onTertiary = Color.White,
    background = DarkGreenBackground,
    onBackground = DarkTextPrimary,
    surface = DarkGreenSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkGreenCard,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkGreenBorder,
    outlineVariant = IslamicGoldDark
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
