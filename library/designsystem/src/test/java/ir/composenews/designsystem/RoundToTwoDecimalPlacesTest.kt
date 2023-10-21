package ir.composenews.designsystem

import ir.composenews.designsystem.extensionFunction.roundToTwoDecimalPlaces
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class RoundToTwoDecimalPlacesTest {
    @Test
    fun testRoundToTwoDecimalPlaces() {
        // Define a map of test cases with input numbers and their expected rounded results
        val testCases = mapOf(
            0.12345 to "0.12",
            1.0 to "1.00",
            1234.5678 to "1234.57",
            0.0f to "0.00",
            BigDecimal("1234.56789") to "1234.57",
        )

        // Iterate through the test cases and assert the expected results
        testCases.forEach { (input, expected) ->
            val result = input.roundToTwoDecimalPlaces()
            Assert.assertEquals(expected, result)
        }
    }
}
