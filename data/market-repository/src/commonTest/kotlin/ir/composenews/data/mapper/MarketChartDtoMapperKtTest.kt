@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.data.mapper

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import ir.composenews.domain.model.MarketChart
import ir.composenews.remotedatasource.dto.MarketChartResponse

class MarketChartDtoMapperKtTest : StringSpec({
    "Given valid market chart response, When converting to chart, Then returns correct chart" {
        val marketChartResponse = MarketChartResponse(
            prices = listOf(
                listOf(-1700000005000.0, 45000.0),
                listOf(1700000005000.0, 46000.5),
            ),
        )

        val marketChart = marketChartResponse.toMarketChart()

        marketChart shouldContainExactly marketChartResponse
    }

    "Given market chart response with empty prices, When converting to chart, Then returns empty chart" {
        val marketChartResponse = MarketChartResponse(prices = emptyList())

        val marketChart = marketChartResponse.toMarketChart()

        marketChart.prices.shouldBeEmpty()
    }

    "Given market chart response with negative timestamps, When converting to chart, Then returns correctly mapped chart" {
        val marketChartResponse = MarketChartResponse(
            prices = listOf(
                listOf(-1000.0, 45000.0),
                listOf(-500.0, 46000.5),
            ),
        )

        val marketChart = marketChartResponse.toMarketChart()

        marketChart shouldContainExactly marketChartResponse
    }

    "Given market chart response with negative prices, When converting to chart, Then returns correctly mapped chart" {
        val marketChartResponse = MarketChartResponse(
            prices = listOf(
                listOf(1700000000000.0, -45000.0),
                listOf(1700000005000.0, -46000.5),
            ),
        )

        val marketChart = marketChartResponse.toMarketChart()

        marketChart shouldContainExactly marketChartResponse
    }

    "Given market chart response with zero values, When converting to chart, Then returns correctly mapped chart" {
        val marketChartResponse = MarketChartResponse(
            prices = listOf(
                listOf(0.0, 0.0),
                listOf(0.0, 0.0),
            ),
        )

        val marketChart = marketChartResponse.toMarketChart()

        marketChart shouldContainExactly marketChartResponse
    }

    "Given market chart response with large double values, When converting to chart, Then returns correctly mapped chart" {
        val marketChartResponse = MarketChartResponse(
            prices = listOf(
                listOf(Double.MAX_VALUE, Double.MAX_VALUE),
                listOf(Double.MIN_VALUE, Double.MIN_VALUE),
            ),
        )

        val marketChart = marketChartResponse.toMarketChart()

        marketChart shouldContainExactly marketChartResponse
    }

    "Given market chart response with non-integer timestamps, When converting to chart, Then truncates decimals" {
        val marketChartResponse = MarketChartResponse(
            prices = listOf(
                listOf(1700000000000.99, 45000.55),
                listOf(1700000005000.49, 46000.99),
            ),
        )

        val marketChart = marketChartResponse.toMarketChart()

        marketChart shouldContainExactly marketChartResponse
    }

    "Given market chart response with missing second element in price pair, When converting to chart, Then throws IndexOutOfBoundsException" {
        val marketChartResponse = MarketChartResponse(
            prices = listOf(
                listOf(1700000000000.0),
            ),
        )

        shouldThrow<IndexOutOfBoundsException> {
            marketChartResponse.toMarketChart()
        }
    }

    "Given market chart response with missing first element in price pair, When converting to chart, Then throws IndexOutOfBoundsException" {
        val marketChartResponse = MarketChartResponse(
            prices = listOf(
                listOf(),
            ),
        )

        shouldThrow<IndexOutOfBoundsException> {
            marketChartResponse.toMarketChart()
        }
    }
})

private infix fun MarketChart.shouldContainExactly(expected: MarketChartResponse) {
    prices.zip(expected.prices).forEach { (actual, expected) ->
        expected.size shouldBe 2
        actual.first shouldBe expected[0].toLong()
        actual.second shouldBe expected[1]
    }
}
