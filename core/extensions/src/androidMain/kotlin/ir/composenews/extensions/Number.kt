@file:Suppress(
    "ImplicitDefaultLocale",
    "ktlint:standard:function-expression-body",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.extensions

fun Number.roundToTwoDecimalPlaces(): String {
    return String.format("%.2f", this)
}
