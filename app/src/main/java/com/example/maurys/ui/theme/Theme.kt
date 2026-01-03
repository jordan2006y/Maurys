// Archivo: app/src/main/java/com/example/maurys/ui/theme/Theme.kt
package com.example.maurys.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de colores oscuro forzado
private val DarkColorScheme = darkColorScheme(
    primary = BeautyPink,        // El color "marca" de la app
    secondary = BeautyPurple,
    tertiary = MoneyGreen,       // Usado para cosas de dinero
    background = SalonBackground,
    surface = SalonSurface,
    onPrimary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun MaurysTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Barra de estado negra para que se funda con el fondo
            window.statusBarColor = SalonBackground.toArgb()
            // Iconos de barra de estado claros (blancos)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}