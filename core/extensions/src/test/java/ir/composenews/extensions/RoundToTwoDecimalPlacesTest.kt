package ir.composenews.extensions

import io.kotest.core.spec.style.StringSpec
import org.junit.Assert
import java.math.BigDecimal

class RoundToTwoDecimalPlacesTest : StringSpec({
    "Round to two decimal places" {
        val testCases = mapOf(
            0.12345 to "0.12",
            1.0 to "1.00",
            1234.5678 to "1234.57",
            0.0f to "0.00",
            BigDecimal("1234.56789") to "1234.57",
        )

        testCases.forEach { (input, expected) ->
            val result = input.roundToTwoDecimalPlaces()
            Assert.assertEquals(expected, result)
        }
    }
})
