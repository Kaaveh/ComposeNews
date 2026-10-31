@file:Suppress(
    "MagicNumber",
    "ktlint"
)

package ir.composenews.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.composenews.designsystem.R

private val SemiBold = FontWeight(600)
private val Heavy = FontWeight(650)

// ponytail: API 24-25 ignores the variation axes and renders the font's default instance (wght 400, wdth 100)
private fun robotoFlex(weight: FontWeight, width: Float = 100f) = Font(
    R.font.roboto_flex,
    weight = weight,
    variationSettings = FontVariation.Settings(
        FontVariation.weight(weight.weight),
        FontVariation.width(width),
    ),
)

/** A family matches by weight only, so each width the design uses has its own family. */
val RobotoFlex = FontFamily(
    robotoFlex(FontWeight.Normal),
    robotoFlex(FontWeight.Medium),
    robotoFlex(SemiBold),
    robotoFlex(Heavy),
    robotoFlex(FontWeight.Bold),
)
private val RobotoFlexDisplay = FontFamily(robotoFlex(SemiBold, width = 110f))
private val RobotoFlexNumeric = FontFamily(robotoFlex(Heavy, width = 104f))

/** Tabular figures, so prices, changes and countdowns don't shift as their digits change. */
fun TextStyle.tabularNums() = copy(fontFeatureSettings = "tnum")

private val Baseline = Typography()

val Typography = Typography(
    displayLarge = Baseline.displayLarge.copy(
        fontFamily = RobotoFlexNumeric,
        fontWeight = Heavy,
        letterSpacing = (-1).sp,
    ).tabularNums(),
    displayMedium = Baseline.displayMedium.copy(
        fontFamily = RobotoFlexNumeric,
        fontWeight = Heavy,
        letterSpacing = (-0.5).sp,
    ).tabularNums(),
    displaySmall = Baseline.displaySmall.copy(
        fontFamily = RobotoFlexDisplay,
        fontWeight = SemiBold,
        letterSpacing = (-0.5).sp,
    ),
    headlineLarge = Baseline.headlineLarge.copy(fontFamily = RobotoFlex),
    headlineMedium = Baseline.headlineMedium.copy(fontFamily = RobotoFlex, fontWeight = SemiBold),
    headlineSmall = Baseline.headlineSmall.copy(fontFamily = RobotoFlex, fontWeight = FontWeight.Medium),
    titleLarge = Baseline.titleLarge.copy(fontFamily = RobotoFlex, fontWeight = FontWeight.Medium),
    titleMedium = Baseline.titleMedium.copy(fontFamily = RobotoFlex, fontWeight = SemiBold),
    titleSmall = Baseline.titleSmall.copy(fontFamily = RobotoFlex),
    bodyLarge = Baseline.bodyLarge.copy(fontFamily = RobotoFlex),
    bodyMedium = Baseline.bodyMedium.copy(fontFamily = RobotoFlex),
    bodySmall = Baseline.bodySmall.copy(fontFamily = RobotoFlex),
    labelLarge = Baseline.labelLarge.copy(fontFamily = RobotoFlex, fontWeight = SemiBold),
    labelMedium = Baseline.labelMedium.copy(fontFamily = RobotoFlex),
    labelSmall = Baseline.labelSmall.copy(fontFamily = RobotoFlex),
)
