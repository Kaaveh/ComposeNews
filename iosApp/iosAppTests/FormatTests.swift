import Foundation
import Shared
import Testing
@testable import ComposeNews

/// The text the screens and the share sheet show. Expectations are in `en_US`, the CI simulator's locale.
struct FormatTests {
    @Test func trendTextPutsTheSignInTheTriangle() {
        #expect(trendText(2.14) == "▲ 2.14%")
        #expect(trendText(-1.37) == "▼ 1.37%")
        #expect(trendText(0) == "▲ 0.00%")
    }

    @Test func priceKeepsSmallPricesReadable() {
        #expect(formatPrice(67_412.8) == "$67,412.80")
        #expect(formatPrice(0.0000123) == "$0.000012")
        // Rounds to $1.00, so it's priced like $1 and up.
        #expect(formatPrice(0.9999996) == "$1.00")
    }

    @Test func retryCountsDownAfterARateLimit() {
        #expect(formatRetry(0) == "Try again")
        #expect(formatRetry(42) == "Try again in 0:42")
    }

    @Test func shareTextHasThePriceTheChangeAndTheLink() {
        let bitcoin = MarketModel(
            id: "bitcoin",
            name: "Bitcoin",
            symbol: "btc",
            currentPrice: 67_412.8,
            priceChangePercentage24h: -2.14,
            imageUrl: "",
            isFavorite: false,
            marketCapRank: nil,
            totalVolume: nil,
            sparkline7d: []
        )

        #expect(shareText(bitcoin) == """
        Bitcoin (BTC): $67,412.80, ▼ 2.14% (24h)
        https://www.coingecko.com/en/coins/bitcoin
        """)
    }
}
