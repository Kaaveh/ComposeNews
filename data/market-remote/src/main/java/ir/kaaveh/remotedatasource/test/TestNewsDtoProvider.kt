package ir.kaaveh.remotedatasource.test

import ir.kaaveh.remotedatasource.dto.MarketResponse

val marketDto = MarketResponse(
    id = "id",
    name = "name",
    symbol = "symbol",
    currentPrice = 100000.0,
    marketCap = 100000.0,
    totalVolume = 100000.0,
    priceChangePercentage24h = 100000.0,
    imageUrl = "some_shit_url.png"
)