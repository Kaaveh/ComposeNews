package ir.composenews.designsystem.component

import io.kotest.core.spec.style.StringSpec
import java.util.TimeZone
import kotlin.test.assertEquals

class FormatTest :
    StringSpec({

        "Given a price, When formatting it, Then it groups thousands and keeps enough decimals for small coins" {
            assertEquals("67,412.80", formatPrice(67_412.8))
            assertEquals("1.00", formatPrice(1.0))
            assertEquals("0.4576", formatPrice(0.4576))
            assertEquals("0.000012", formatPrice(0.0000123))
            assertEquals("0", formatPrice(0.0))
            assertEquals("1.00", formatPrice(0.9999996))
            assertEquals("67,412.80 $", formatUsdPrice(67_412.8))
            assertEquals("1.37 %", formatPercent(-1.37))
        }

        "Given a market cap, When formatting it compactly, Then it keeps three significant figures" {
            assertEquals("$1.33T", formatCompactUsd(1_330_000_000_000))
            assertEquals("$255B", formatCompactUsd(255_000_000_000))
            assertEquals("$12.4M", formatCompactUsd(12_400_000))
            assertEquals("$950,000", formatCompactUsd(950_000))
            assertEquals("$1.00T", formatCompactUsd(999_960_000_000))
            assertEquals("$100M", formatCompactUsd(99_960_000))
            assertEquals("$10.0B", formatCompactUsd(9_996_000_000))
        }

        "Given a 429 countdown, When labeling the retry button, Then it shows minutes and seconds until it ends" {
            assertEquals("Try again", formatRetry(0))
            assertEquals("Try again in 0:42", formatRetry(42))
            assertEquals("Try again in 1:05", formatRetry(65))
        }

        "Given a chart point, When formatting its time, Then recent days read as words and older ones as dates" {
            val utc = TimeZone.getTimeZone("UTC")
            val now = 1_761_920_400_000 // Oct 31, 2025 14:20 UTC
            assertEquals("Today, 14:20", formatChartTime(now, now, utc))
            assertEquals("Today, 00:00", formatChartTime(1_761_868_800_000, now, utc))
            assertEquals("Yesterday, 23:05", formatChartTime(1_761_865_500_000, now, utc))
            assertEquals("Oct 24, 14:00", formatChartTime(1_761_314_400_000, now, utc))
            assertEquals("Mar 3, 2024", formatChartTime(1_709_424_000_000, now, utc))
        }
    })
