package ir.kaaveh.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ir.kaaveh.navigation.Destinations
import ir.kaaveh.navigation.extension_function.parcelableData
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.domain.model.Market
import ir.kaaveh.marketdetail.NewsDetailRoute

fun NavGraphBuilder.newsDetail(
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    composable(
        route = Destinations.NewsDetailScreen().route,
    ) { entry ->
        val news = entry.parcelableData<Market>(Destinations.NewsDetailScreen().news)
        NewsDetailRoute(
            news = news,
            onProvideBaseViewModel = {
                onProvideBaseViewModel(it)
            },
        )
    }
}