import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ir.composenews.domain.model.Market
import ir.composenews.uimarket.mapper.toMarket
import ir.composenews.uimarket.mapper.toMarketModel
import ir.composenews.uimarket.model.MarketModel

class MarketModelMapperTest {

    @Test
    fun `test MarketModel to Market mapping`() {

        val marketModel = MarketModel(
            id = "1",
            name = "Bitcoin",
            symbol = "BTC",
            currentPrice = 20000.0,
            priceChangePercentage24h = 5.0,
            imageUrl = "https://pngimg.com/image/36992",
            isFavorite = true
        )

        val market = marketModel.toMarket()

        assertEquals(marketModel.id, market.id)
        assertEquals(marketModel.name, market.name)
        assertEquals(marketModel.symbol, market.symbol)
        assertEquals(marketModel.currentPrice, market.currentPrice)
        assertEquals(marketModel.priceChangePercentage24h, market.priceChangePercentage24h)
        assertEquals(marketModel.imageUrl, market.imageUrl)
        assertEquals(marketModel.isFavorite, market.isFavorite)
    }

    @Test
    fun `test Market to MarketModel mapping`() {

        val market = Market(
            id = "1",
            name = "Bitcoin",
            symbol = "BTC",
            currentPrice = 20000.0,
            priceChangePercentage24h = 5.0,
            imageUrl = "https://pngimg.com/image/36992",
            isFavorite = true
        )

        val marketModel = market.toMarketModel()

        assertEquals(market.id, marketModel.id)
        assertEquals(market.name, marketModel.name)
        assertEquals(market.symbol, marketModel.symbol)
        assertEquals(market.currentPrice, marketModel.currentPrice)
        assertEquals(market.priceChangePercentage24h, marketModel.priceChangePercentage24h)
        assertEquals(market.imageUrl, marketModel.imageUrl)
        assertEquals(market.isFavorite, marketModel.isFavorite)
    }
}