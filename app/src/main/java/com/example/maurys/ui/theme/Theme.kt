package com.example.maurys.ui.theme

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

// Esquema Oscuro (El que más se usará)
private val DarkColorScheme = darkColorScheme(
    primary = MainBlue,
    secondary = MainBlue,
    tertiary = Crema,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// Esquema Claro (Por si acaso)
private val LightColorScheme = lightColorScheme(
    primary = MainBlue,
    secondary = MainBlue,
    tertiary = Crema,
    background = MainGray,
    surface = MainWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = MainBlack,
    onSurface = MainBlack
)

@Composable
fun MaurysTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivamos el color dinámico para forzar TU diseño (Azul/Negro)
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Lógica para la barra de estado transparente
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate de tener Typography en Type.kt (por defecto sirve)
        content = content
    )
}