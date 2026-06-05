package ir.composenews.apptv.ui.mock

import ir.composenews.uimarket.model.MarketModel

val fakeMarkets: List<MarketModel> =
    listOf(
        MarketModel(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "btc",
            currentPrice = 67_842.15,
            priceChangePercentage24h = 2.14,
            imageUrl = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
            isFavorite = true,
        ),
        MarketModel(
            id = "ethereum",
            name = "Ethereum",
            symbol = "eth",
            currentPrice = 3_512.78,
            priceChangePercentage24h = -1.32,
            imageUrl = "https://assets.coingecko.com/coins/images/279/large/ethereum.png",
        ),
        MarketModel(
            id = "tether",
            name = "Tether",
            symbol = "usdt",
            currentPrice = 1.00,
            priceChangePercentage24h = 0.01,
            imageUrl = "https://assets.coingecko.com/coins/images/325/large/Tether.png",
        ),
        MarketModel(
            id = "solana",
            name = "Solana",
            symbol = "sol",
            currentPrice = 168.42,
            priceChangePercentage24h = 4.87,
            imageUrl = "https://assets.coingecko.com/coins/images/4128/large/solana.png",
            isFavorite = true,
        ),
        MarketModel(
            id = "binancecoin",
            name = "BNB",
            symbol = "bnb",
            currentPrice = 612.30,
            priceChangePercentage24h = -0.54,
            imageUrl = "https://assets.coingecko.com/coins/images/825/large/bnb-icon2_2x.png",
        ),
        MarketModel(
            id = "ripple",
            name = "XRP",
            symbol = "xrp",
            currentPrice = 0.58,
            priceChangePercentage24h = 1.92,
            imageUrl = "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png",
        ),
        MarketModel(
            id = "cardano",
            name = "Cardano",
            symbol = "ada",
            currentPrice = 0.42,
            priceChangePercentage24h = -2.18,
            imageUrl = "https://assets.coingecko.com/coins/images/975/large/cardano.png",
        ),
        MarketModel(
            id = "dogecoin",
            name = "Dogecoin",
            symbol = "doge",
            currentPrice = 0.158,
            priceChangePercentage24h = 5.63,
            imageUrl = "https://assets.coingecko.com/coins/images/5/large/dogecoin.png",
        ),
    )

val fakeFavoriteMarkets: List<MarketModel> = fakeMarkets.filter { it.isFavorite }
