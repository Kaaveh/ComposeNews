@file:Suppress("MagicNumber")

package ir.composenews.designsystem.component

import java.math.BigDecimal
import java.math.MathContext
import java.util.Locale
import kotlin.math.abs

private const val SMALL_PRICE_DECIMALS = "%.6f"

/** `67,412.80` from 1 up; below 1, up to six decimals without trailing zeros (`0.4576`). Always Latin digits. */
fun formatPrice(value: Double): String {
    val small = String.format(Locale.US, SMALL_PRICE_DECIMALS, value)
    // Decide on the rounded value, so 0.9999996 is `1.00` rather than `1`.
    return if (small.toDouble() >= 1) {
        String.format(Locale.US, "%,.2f", value)
    } else {
        small.trimEnd('0').trimEnd('.')
    }
}

/** A price with its currency, as every screen shows it: `67,412.80 $`. */
fun formatUsdPrice(value: Double): String = "${formatPrice(value)} $"

/** The size of a percent change, without its sign: `2.14 %`. The sign is the arrow next to it. */
fun formatPercent(change: Double): String = String.format(Locale.US, "%.2f %%", abs(change))

/** A dollar amount in three significant figures with a T, B or M suffix: `$1.33T`, `$255B`, `$12.4M`. */
fun formatCompactUsd(value: Long): String {
    // Round first, so 999.96B becomes `$1.00T` rather than `$1000B`.
    val rounded = BigDecimal(value).round(MathContext(3)).toDouble()
    val (scaled, suffix) =
        when {
            rounded >= 1e12 -> rounded / 1e12 to "T"
            rounded >= 1e9 -> rounded / 1e9 to "B"
            rounded >= 1e6 -> rounded / 1e6 to "M"
            else -> return "$" + String.format(Locale.US, "%,d", value)
        }
    val decimals =
        when {
            scaled >= 100 -> 0
            scaled >= 10 -> 1
            else -> 2
        }
    return "$" + String.format(Locale.US, "%.${decimals}f", scaled) + suffix
}
