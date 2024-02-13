package ir.composenews.uimarket.mapper

import ir.composenews.domain.model.Market
import ir.composenews.uimarket.model.MarketModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MarketModelMapperTest {

    companion object {
        // Constants for test data
        private const val ID = "1"
        private const val NAME = "Bitcoin"
        private const val SYMBOL = "BTC"
        private const val CURRENT_PRICE = 20000.0
        private const val PRICE_CHANGE_PERCENTAGE_24H = 5.0
        private const val IMAGE_URL = "https://pngimg.com/image/36992"
        private const val IS_FAVORITE = true
    }

    @Test
    fun `test MarketModel to Market mapping`() {
        val marketModel = MarketModel(
            id = ID,
            name = NAME,
            symbol = SYMBOL,
            currentPrice = CURRENT_PRICE,
            priceChangePercentage24h = PRICE_CHANGE_PERCENTAGE_24H,
            imageUrl = IMAGE_URL,
            isFavorite = IS_FAVORITE,
        )

        val market = marketModel.toMarket()

        assertEquals(ID, market.id)
        assertEquals(NAME, market.name)
        assertEquals(SYMBOL, market.symbol)
        assertEquals(CURRENT_PRICE, market.currentPrice)
        assertEquals(PRICE_CHANGE_PERCENTAGE_24H, market.priceChangePercentage24h)
        assertEquals(IMAGE_URL, market.imageUrl)
        assertEquals(IS_FAVORITE, market.isFavorite)
    }

    @Test
    fun `test Market to MarketModel mapping`() {
        val market = Market(
            id = ID,
            name = NAME,
            symbol = SYMBOL,
            currentPrice = CURRENT_PRICE,
            priceChangePercentage24h = PRICE_CHANGE_PERCENTAGE_24H,
            imageUrl = IMAGE_URL,
            isFavorite = IS_FAVORITE,
        )

        val marketModel = market.toMarketModel()

        assertEquals(ID, marketModel.id)
        assertEquals(NAME, marketModel.name)
        assertEquals(SYMBOL, marketModel.symbol)
        assertEquals(CURRENT_PRICE, marketModel.currentPrice)
        assertEquals(PRICE_CHANGE_PERCENTAGE_24H, marketModel.priceChangePercentage24h)
        assertEquals(IMAGE_URL, marketModel.imageUrl)
        assertEquals(IS_FAVORITE, marketModel.isFavorite)
    }
}
