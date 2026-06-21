package com.notevault.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val WarmLightColorScheme = lightColorScheme(
    primary = SoftBrown,
    onPrimary = Color.White,
    primaryContainer = ParchmentLight,
    onPrimaryContainer = DeepBrown,
    secondary = ForestGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD7E8D8),
    onSecondaryContainer = Color(0xFF1B3B1C),
    tertiary = WarmGold,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFF0D4),
    onTertiaryContainer = Color(0xFF4A3800),
    background = Cream,
    onBackground = DeepBrown,
    surface = WarmWhite,
    onSurface = DeepBrown,
    surfaceVariant = ParchmentLight,
    onSurfaceVariant = Color(0xFF6B5E52),
    outline = Color(0xFFAA9D91),
    outlineVariant = Color(0xFFD4C9BD),
)

private val WarmDarkColorScheme = darkColorScheme(
    primary = GoldOnDark,
    onPrimary = Color(0xFF3D2E00),
    primaryContainer = DarkCard,
    onPrimaryContainer = CreamOnDark,
    secondary = Color(0xFF8FBF90),
    onSecondary = Color(0xFF1B3B1C),
    secondaryContainer = Color(0xFF2E4F2F),
    onSecondaryContainer = Color(0xFFD7E8D8),
    tertiary = Color(0xFFDEB558),
    onTertiary = Color(0xFF3D2E00),
    tertiaryContainer = Color(0xFF574300),
    onTertiaryContainer = Color(0xFFFFF0D4),
    background = DarkCharcoal,
    onBackground = CreamOnDark,
    surface = DarkBrown,
    onSurface = CreamOnDark,
    surfaceVariant = DarkCard,
    onSurfaceVariant = Color(0xFFCFC4B8),
    outline = Color(0xFF8A7F73),
    outlineVariant = Color(0xFF4D453D),
)

@Composable
fun NoteVaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Off by default for warm palette
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> WarmDarkColorScheme
        else -> WarmLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NoteVaultTypography,
        content = content
    )
}
