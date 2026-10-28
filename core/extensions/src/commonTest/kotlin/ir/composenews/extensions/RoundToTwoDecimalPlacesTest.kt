@file:Suppress(
    "ktlint:standard:class-signature",
    "ktlint:standard:multiline-expression-wrapping",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.extensions

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class RoundToTwoDecimalPlacesTest : StringSpec({
    "Round to two decimal places" {
        val testCases = mapOf<Number, String>(
            0.12345 to "0.12",
            1.0 to "1.00",
            1234.5678 to "1234.57",
            0.0f to "0.00",
            5 to "5.00",
            -2.345678 to "-2.35",
            -0.05 to "-0.05",
            0.999 to "1.00",
            0.015 to "0.02",
            -0.015 to "-0.02",
            -0.125 to "-0.13",
            1.005 to "1.01",
            0.285 to "0.29",
            9.995 to "10.00",
            -0.001 to "0.00",
            0.00001 to "0.00",
            12345678.9 to "12345678.90",
            1e20 to "100000000000000000000.00",
            Double.NaN to "NaN",
            Double.NEGATIVE_INFINITY to "-Infinity",
        )

        testCases.forEach { (input, expected) ->
            val result = input.roundToTwoDecimalPlaces()
            result shouldBeEqual expected
        }
    }
})
