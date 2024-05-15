@file:Suppress("ImplicitDefaultLocale")

package ir.composenews.extensions

fun Number.roundToTwoDecimalPlaces(): String {
    return String.format("%.2f", this)
}
