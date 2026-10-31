@file:Suppress("MagicNumber", "MaxLineLength", "LongMethod", "LongParameterList", "UnusedPrivateMember", "ktlint:standard:function-naming")

package ir.composenews.apptv.ui

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Typography
import androidx.tv.material3.darkColorScheme
import ir.composenews.designsystem.theme.ComposeNewsTheme
import androidx.compose.material3.MaterialTheme as PhoneMaterialTheme

/**
 * The TV app is dark only (#389 E1). tv-material components read their own theme, so it gets the phone theme's
 * colors and type rather than a second palette.
 */
@Composable
fun ComposeNewsTvTheme(content: @Composable () -> Unit) {
    ComposeNewsTheme(useDarkTheme = true) {
        val colors = PhoneMaterialTheme.colorScheme
        val type = PhoneMaterialTheme.typography
        val tvColors =
            remember(colors) {
                darkColorScheme(
                    primary = colors.primary,
                    onPrimary = colors.onPrimary,
                    primaryContainer = colors.primaryContainer,
                    onPrimaryContainer = colors.onPrimaryContainer,
                    inversePrimary = colors.inversePrimary,
                    secondary = colors.secondary,
                    onSecondary = colors.onSecondary,
                    secondaryContainer = colors.secondaryContainer,
                    onSecondaryContainer = colors.onSecondaryContainer,
                    tertiary = colors.tertiary,
                    onTertiary = colors.onTertiary,
                    tertiaryContainer = colors.tertiaryContainer,
                    onTertiaryContainer = colors.onTertiaryContainer,
                    background = colors.surfaceContainerLowest,
                    onBackground = colors.onBackground,
                    surface = colors.surfaceContainerLowest,
                    onSurface = colors.onSurface,
                    surfaceVariant = colors.surfaceContainerLow,
                    onSurfaceVariant = colors.onSurfaceVariant,
                    inverseSurface = colors.inverseSurface,
                    inverseOnSurface = colors.inverseOnSurface,
                    error = colors.error,
                    onError = colors.onError,
                    errorContainer = colors.errorContainer,
                    onErrorContainer = colors.onErrorContainer,
                    border = colors.outline,
                    borderVariant = colors.outlineVariant,
                    scrim = colors.scrim,
                )
            }
        val tvType =
            remember(type) {
                Typography(
                    displayLarge = type.displayLarge,
                    displayMedium = type.displayMedium,
                    displaySmall = type.displaySmall,
                    headlineLarge = type.headlineLarge,
                    headlineMedium = type.headlineMedium,
                    headlineSmall = type.headlineSmall,
                    titleLarge = type.titleLarge,
                    titleMedium = type.titleMedium,
                    titleSmall = type.titleSmall,
                    bodyLarge = type.bodyLarge,
                    bodyMedium = type.bodyMedium,
                    bodySmall = type.bodySmall,
                    labelLarge = type.labelLarge,
                    labelMedium = type.labelMedium,
                    labelSmall = type.labelSmall,
                )
            }
        // Phone components (StatePane, TrendChange) read the phone content color, which no phone Surface sets here.
        CompositionLocalProvider(LocalContentColor provides colors.onSurface) {
            MaterialTheme(colorScheme = tvColors, typography = tvType, content = content)
        }
    }
}
