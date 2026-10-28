package ir.composenews.extensions

import kotlin.math.absoluteValue

private const val DECIMALS = 2

/**
 * Rounds half away from zero on the shortest decimal form, like `String.format("%.2f")`,
 * so `1.005` gives `1.01` even though its binary value is just below it.
 * Always uses `.` and Latin digits, whatever the device locale.
 */
fun Number.roundToTwoDecimalPlaces(): String {
    val value = toDouble()
    if (!value.isFinite()) return value.toString()

    val parts =
        value.absoluteValue
            .toString()
            .lowercase()
            .split('e')
    val mantissa = parts[0]
    val exponent = parts.getOrNull(1)?.toInt() ?: 0
    var digits = mantissa.replace(".", "")
    // How many leading digits make up the value in cents.
    var keep = (mantissa.indexOf('.').takeIf { it >= 0 } ?: mantissa.length) + exponent + DECIMALS
    if (keep < 1) {
        digits = "0".repeat(1 - keep) + digits
        keep = 1
    }

    val truncated = digits.take(keep).padEnd(keep, '0')
    val rounded = if (digits.getOrElse(keep) { '0' } >= '5') truncated.incremented() else truncated
    val cents = rounded.padStart(DECIMALS + 1, '0')
    val sign = if (value < 0 && cents.any { it != '0' }) "-" else ""
    return "$sign${cents.dropLast(DECIMALS).trimStart('0').ifEmpty { "0" }}.${cents.takeLast(DECIMALS)}"
}

/** Adds one to a string of decimal digits. */
private fun String.incremented(): String {
    val last = indexOfLast { it != '9' }
    if (last < 0) return "1" + "0".repeat(length)
    return take(last) + (this[last] + 1) + "0".repeat(length - last - 1)
}
