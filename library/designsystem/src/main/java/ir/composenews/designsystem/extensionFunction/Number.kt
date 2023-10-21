package ir.composenews.designsystem.extensionFunction

fun Number.roundToTwoDecimalPlaces(): String {
    return String.format("%.2f", this)
}
