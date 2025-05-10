package com.bysoftware.fixedcalendar.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.bysoftware.fixedcalendar.ui.viewmodel.SettingsViewModel

fun createDarkColorScheme(primaryColor: Color) = darkColorScheme(
    primary = primaryColor,
    secondary = primaryColor.copy(alpha = 0.8f),
    tertiary = primaryColor.copy(alpha = 0.6f),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = primaryColor.copy(alpha = 0.2f),
    secondaryContainer = primaryColor.copy(alpha = 0.1f),
    tertiaryContainer = primaryColor.copy(alpha = 0.15f),
    onPrimaryContainer = Color.White,
    onSecondaryContainer = Color.White,
    onTertiaryContainer = Color.White,
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFE0E0E0),
    outline = Color(0xFF9E9E9E),
    outlineVariant = Color(0xFF616161),
    scrim = Color(0xFF000000)
)

fun createLightColorScheme(primaryColor: Color) = lightColorScheme(
    primary = primaryColor,
    secondary = primaryColor.copy(alpha = 0.8f),
    tertiary = primaryColor.copy(alpha = 0.6f),
    background = Color(0xFFFAFAFA),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    primaryContainer = primaryColor.copy(alpha = 0.1f),
    secondaryContainer = primaryColor.copy(alpha = 0.05f),
    tertiaryContainer = primaryColor.copy(alpha = 0.08f),
    onPrimaryContainer = primaryColor.copy(alpha = 0.8f),
    onSecondaryContainer = primaryColor.copy(alpha = 0.7f),
    onTertiaryContainer = primaryColor.copy(alpha = 0.75f),
    surfaceVariant = primaryColor.copy(alpha = 0.05f),
    onSurfaceVariant = primaryColor.copy(alpha = 0.8f),
    outline = primaryColor.copy(alpha = 0.5f),
    outlineVariant = primaryColor.copy(alpha = 0.3f),
    scrim = Color(0xFF000000)
)

private val DefaultDarkColorScheme = darkColorScheme(
    primary = Color(0xFFE57373),
    secondary = Color(0xFFFF8A80),
    tertiary = Color(0xFFEF5350),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = Color(0xFF731010),
    secondaryContainer = Color(0xFF5D1C1C),
    tertiaryContainer = Color(0xFF731010),
    onPrimaryContainer = Color.White,
    onSecondaryContainer = Color.White,
    onTertiaryContainer = Color.White,
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFE0E0E0),
    outline = Color(0xFF9E9E9E),
    outlineVariant = Color(0xFF616161),
    scrim = Color(0xFF000000)
)

private val DefaultLightColorScheme = lightColorScheme(
    primary = Color(0xFFD32F2F),
    secondary = Color(0xFFE57373),
    tertiary = Color(0xFFEF5350),
    background = Color(0xFFFAFAFA),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    primaryContainer = Color(0xFFFFEBEE),
    secondaryContainer = Color(0xFFFFCDD2),
    tertiaryContainer = Color(0xFFFFEBEE),
    onPrimaryContainer = Color(0xFF731010),
    onSecondaryContainer = Color(0xFF5D1C1C),
    onTertiaryContainer = Color(0xFF731010),
    surfaceVariant = Color(0xFFFFEBEE),
    onSurfaceVariant = Color(0xFF731010),
    outline = Color(0xFFB71C1C),
    outlineVariant = Color(0xFFEF5350),
    scrim = Color(0xFF000000)
)

@Composable
fun FixedCalendarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
    val useCustomTheme by settingsViewModel.useCustomTheme.collectAsState()
    val customPrimaryColor by settingsViewModel.customPrimaryColor.collectAsState()

    val colorScheme = when {
        useCustomTheme -> {
            if (isDarkMode) createDarkColorScheme(customPrimaryColor)
            else createLightColorScheme(customPrimaryColor)
        }
        isDarkMode -> DefaultDarkColorScheme
        else -> DefaultLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkMode
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}

@Composable
fun PreviewFixedCalendarTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DefaultDarkColorScheme else DefaultLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}