package com.example.myapplication.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.myapplication.R

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val currentConfig = LocalConfiguration.current
    val configuration = remember(darkTheme, currentConfig) {
        Configuration(currentConfig).apply {
            uiMode = if (darkTheme) {
                (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or Configuration.UI_MODE_NIGHT_YES
            } else {
                (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or Configuration.UI_MODE_NIGHT_NO
            }
        }
    }
    val contextWithNightMode = remember(darkTheme, context, configuration) {
        context.createConfigurationContext(configuration)
    }

    CompositionLocalProvider(LocalContext provides contextWithNightMode) {
        val colorScheme = if (darkTheme) {
            darkColorScheme(
                primary = colorResource(R.color.primary),
                onPrimary = colorResource(R.color.on_primary),
                secondary = colorResource(R.color.secondary),
                background = colorResource(R.color.background),
                onBackground = colorResource(R.color.on_background),
                surface = colorResource(R.color.surface),
                onSurface = colorResource(R.color.on_surface),
                error = colorResource(R.color.error)
            )
        } else {
            lightColorScheme(
                primary = colorResource(R.color.primary),
                onPrimary = colorResource(R.color.on_primary),
                secondary = colorResource(R.color.secondary),
                background = colorResource(R.color.background),
                onBackground = colorResource(R.color.on_background),
                surface = colorResource(R.color.surface),
                onSurface = colorResource(R.color.on_surface),
                error = colorResource(R.color.error)
            )
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}