@file:Suppress("ktlint:standard:function-naming")

package ir.composenews.appwatch.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Typography
import ir.composenews.designsystem.theme.DarkExtendedColors
import ir.composenews.designsystem.theme.LocalExtendedColors
import ir.composenews.designsystem.theme.RobotoFlex
import ir.composenews.designsystem.theme.md_theme_dark_error
import ir.composenews.designsystem.theme.md_theme_dark_errorContainer
import ir.composenews.designsystem.theme.md_theme_dark_onBackground
import ir.composenews.designsystem.theme.md_theme_dark_onError
import ir.composenews.designsystem.theme.md_theme_dark_onErrorContainer
import ir.composenews.designsystem.theme.md_theme_dark_onPrimary
import ir.composenews.designsystem.theme.md_theme_dark_onPrimaryContainer
import ir.composenews.designsystem.theme.md_theme_dark_onSecondary
import ir.composenews.designsystem.theme.md_theme_dark_onSecondaryContainer
import ir.composenews.designsystem.theme.md_theme_dark_onSurface
import ir.composenews.designsystem.theme.md_theme_dark_onSurfaceVariant
import ir.composenews.designsystem.theme.md_theme_dark_onTertiary
import ir.composenews.designsystem.theme.md_theme_dark_onTertiaryContainer
import ir.composenews.designsystem.theme.md_theme_dark_outline
import ir.composenews.designsystem.theme.md_theme_dark_outlineVariant
import ir.composenews.designsystem.theme.md_theme_dark_primary
import ir.composenews.designsystem.theme.md_theme_dark_primaryContainer
import ir.composenews.designsystem.theme.md_theme_dark_secondary
import ir.composenews.designsystem.theme.md_theme_dark_secondaryContainer
import ir.composenews.designsystem.theme.md_theme_dark_surfaceContainer
import ir.composenews.designsystem.theme.md_theme_dark_surfaceContainerHigh
import ir.composenews.designsystem.theme.md_theme_dark_tertiary
import ir.composenews.designsystem.theme.md_theme_dark_tertiaryContainer

/** The phone's dark crimson scheme on a black watch face; titles use the light pink [ColorScheme.tertiary]. */
private val WearColorScheme =
    ColorScheme(
        primary = md_theme_dark_primary,
        primaryContainer = md_theme_dark_primaryContainer,
        onPrimary = md_theme_dark_onPrimary,
        onPrimaryContainer = md_theme_dark_onPrimaryContainer,
        secondary = md_theme_dark_secondary,
        secondaryContainer = md_theme_dark_secondaryContainer,
        onSecondary = md_theme_dark_onSecondary,
        onSecondaryContainer = md_theme_dark_onSecondaryContainer,
        tertiary = md_theme_dark_tertiary,
        tertiaryContainer = md_theme_dark_tertiaryContainer,
        onTertiary = md_theme_dark_onTertiary,
        onTertiaryContainer = md_theme_dark_onTertiaryContainer,
        surfaceContainerLow = md_theme_dark_surfaceContainer,
        surfaceContainer = md_theme_dark_surfaceContainer,
        surfaceContainerHigh = md_theme_dark_surfaceContainerHigh,
        onSurface = md_theme_dark_onSurface,
        onSurfaceVariant = md_theme_dark_onSurfaceVariant,
        outline = md_theme_dark_outline,
        outlineVariant = md_theme_dark_outlineVariant,
        background = Color.Black,
        onBackground = md_theme_dark_onBackground,
        error = md_theme_dark_error,
        errorContainer = md_theme_dark_errorContainer,
        onError = md_theme_dark_onError,
        onErrorContainer = md_theme_dark_onErrorContainer,
    )

private val WearTypography = Typography(defaultFontFamily = RobotoFlex)

@Composable
fun ComposeNewsWearTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalExtendedColors provides DarkExtendedColors) {
        MaterialTheme(colorScheme = WearColorScheme, typography = WearTypography, content = content)
    }
}
