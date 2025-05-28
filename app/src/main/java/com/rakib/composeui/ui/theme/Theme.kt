package com.rakib.composeui.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = WhatsAppGreen,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = WhatsAppBackground
)

private val LightColorScheme = lightColorScheme(
    primary = WhatsAppGreen,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = WhatsAppBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun WhatsAppCloneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}