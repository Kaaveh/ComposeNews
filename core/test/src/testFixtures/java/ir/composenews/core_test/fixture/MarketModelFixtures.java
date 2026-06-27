package ir.composenews.core_test.fixture;

import ir.composenews.uimarket.model.MarketModel;

public final class MarketModelFixtures {
    private MarketModelFixtures() {
    }

    public static MarketModel buildMarketModel() {
        return buildMarketModel(
                "btc",
                "Bitcoin",
                "BTC",
                50000.0,
                2.5,
                "",
                false
        );
    }

    public static MarketModel buildMarketModel(boolean isFavorite) {
        return buildMarketModel(
                "btc",
                "Bitcoin",
                "BTC",
                50000.0,
                2.5,
                "",
                isFavorite
        );
    }

    public static MarketModel buildMarketModel(String id, String name, String symbol) {
        return buildMarketModel(
                id,
                name,
                symbol,
                50000.0,
                2.5,
                "",
                false
        );
    }

    public static MarketModel buildMarketModel(
            String id,
            String name,
            String symbol,
            double currentPrice,
            double priceChangePercentage24h,
            String imageUrl,
            boolean isFavorite
    ) {
        return new MarketModel(
                id,
                name,
                symbol,
                currentPrice,
                priceChangePercentage24h,
                imageUrl,
                isFavorite
        );
    }
}
