@file:Suppress("NoConsecutiveBlankLines", "MagicNumber")

package ir.composenews.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val md_theme_light_primary = Color(0xffdd0d3c)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFFFDADC)
val md_theme_light_onPrimaryContainer = Color(0xFF400010)
val md_theme_light_secondary = Color(0xffdd0d3c)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFFFDAD9)
val md_theme_light_onSecondaryContainer = Color(0xFF40000A)
val md_theme_light_tertiary = Color(0xFF984060)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFD9E2)
val md_theme_light_onTertiaryContainer = Color(0xFF3E001D)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFFFBFF)
val md_theme_light_onBackground = Color(0xFF201A1A)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF201A1A)
val md_theme_light_surfaceVariant = Color(0xFFF4DDDE)
val md_theme_light_onSurfaceVariant = Color(0xFF524344)
val md_theme_light_outline = Color(0xFF857374)
val md_theme_light_inverseOnSurface = Color(0xFFFBEEEE)
val md_theme_light_inverseSurface = Color(0xFF362F2F)
val md_theme_light_inversePrimary = Color(0xFFFFB2B9)
val md_theme_light_surfaceTint = Color(0xFFA4384A)
val md_theme_light_outlineVariant = Color(0xFFD7C1C2)
val md_theme_light_scrim = Color(0xFF000000)
val md_theme_light_surfaceDim = Color(0xFFE3D7D7)
val md_theme_light_surfaceBright = Color(0xFFFFFBFF)
val md_theme_light_surfaceContainerLowest = Color(0xFFFFFFFF)
val md_theme_light_surfaceContainerLow = Color(0xFFF9ECED)
val md_theme_light_surfaceContainer = Color(0xFFF6E5E5)
val md_theme_light_surfaceContainerHigh = Color(0xFFF5E1E2)
val md_theme_light_surfaceContainerHighest = Color(0xFFF4DDDE)

val md_theme_dark_primary = Color(0xffea6d7e)
val md_theme_dark_onPrimary = Color(0xFF000000)
val md_theme_dark_primaryContainer = Color(0xFF852034)
val md_theme_dark_onPrimaryContainer = Color(0xFFFFDADC)
val md_theme_dark_secondary = Color(0xffea6d7e)
val md_theme_dark_onSecondary = Color(0xFF000000)
val md_theme_dark_secondaryContainer = Color(0xFF920023)
val md_theme_dark_onSecondaryContainer = Color(0xFFFFDAD9)
val md_theme_dark_tertiary = Color(0xFFFFB1C7)
val md_theme_dark_onTertiary = Color(0xFF5E1132)
val md_theme_dark_tertiaryContainer = Color(0xFF7B2948)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFD9E2)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF201A1A)
val md_theme_dark_onBackground = Color(0xFFECE0E0)
val md_theme_dark_surface = Color(0xFF201A1A)
val md_theme_dark_onSurface = Color(0xFFECE0E0)
val md_theme_dark_surfaceVariant = Color(0xFF524344)
val md_theme_dark_onSurfaceVariant = Color(0xFFD7C1C2)
val md_theme_dark_outline = Color(0xFF9F8C8D)
val md_theme_dark_inverseOnSurface = Color(0xFF201A1A)
val md_theme_dark_inverseSurface = Color(0xFFECE0E0)
val md_theme_dark_inversePrimary = Color(0xFFA4384A)
val md_theme_dark_surfaceTint = Color(0xFFFFB2B9)
val md_theme_dark_outlineVariant = Color(0xFF524344)
val md_theme_dark_scrim = Color(0xFF000000)
val md_theme_dark_surfaceDim = Color(0xFF201A1A)
val md_theme_dark_surfaceBright = Color(0xFF483F3F)
val md_theme_dark_surfaceContainerLowest = Color(0xFF1A1314)
val md_theme_dark_surfaceContainerLow = Color(0xFF2D2425)
val md_theme_dark_surfaceContainer = Color(0xFF2D2425)
val md_theme_dark_surfaceContainerHigh = Color(0xFF352B2C)
val md_theme_dark_surfaceContainerHighest = Color(0xFF3D3233)

/** Roles the Material scheme has no slot for. Read them through `MaterialTheme.extendedColors`. */
@Immutable
data class ExtendedColors(
    val trendUp: Color,
    val trendDown: Color,
    val trendUpContainer: Color,
    val onTrendUpContainer: Color,
    val favoriteOn: Color,
    val favoriteOff: Color,
    val chartLine: Color,
)

val LightExtendedColors =
    ExtendedColors(
        trendUp = Color(0xFF1B7F3A),
        trendDown = Color(0xFFC4001A),
        trendUpContainer = Color(0xFFC9EFD2),
        onTrendUpContainer = Color(0xFF0B4F22),
        favoriteOn = Color(0xFFFF0000),
        favoriteOff = Color(0xFF9E8C8D),
        chartLine = Color(0xFF6750A4),
    )

val DarkExtendedColors =
    ExtendedColors(
        trendUp = Color(0xFF5BD38A),
        trendDown = Color(0xFFFF8F8F),
        trendUpContainer = Color(0xFF0F3D20),
        onTrendUpContainer = Color(0xFF8FF0B0),
        favoriteOn = Color(0xFFFF0000),
        favoriteOff = Color(0xFF9E8C8D),
        chartLine = Color(0xFFB9A7F0),
    )

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColors.current
