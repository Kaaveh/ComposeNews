package ir.composenews.navigation

sealed class Destinations(val route: String) {
    data object MarketListScreen : Destinations("market_list_screen")
    data class MarketDetailScreen(val market: String = "market") :
        Destinations("market_detail_screen")

    data object FavoriteMarketScreen : Destinations("favorite_market_list_screen")
}
