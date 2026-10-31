package ir.composenews.designsystem.component

import io.kotest.core.spec.style.StringSpec
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
    })
