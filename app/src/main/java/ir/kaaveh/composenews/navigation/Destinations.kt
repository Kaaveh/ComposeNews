package ir.kaaveh.composenews.navigation

sealed class Destinations(val route: String) {
    object NewsListScreen : Destinations("news_list_screen")
    data class NewsDetailScreen(val news: String = "news") : Destinations("news_detail_screen")
    object FavoriteNewsScreen : Destinations("favorite_news_screen")
}