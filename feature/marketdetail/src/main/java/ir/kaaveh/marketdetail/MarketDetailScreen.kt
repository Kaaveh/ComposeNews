package ir.kaaveh.marketdetail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.designsystem.component.FavoriteIcon
import ir.kaaveh.designsystem.preview.ThemePreviews
import ir.kaaveh.designsystem.theme.ComposeNewsTheme
import ir.kaaveh.designsystem.use
import ir.kaaveh.domain.model.Market
import ir.kaaveh.marketdetail.preview_provider.MarketDetailStateProvider

@Composable
fun MarketDetailRoute(
    market: Market?,
    viewModel: MarketDetailViewModel = hiltViewModel(),
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    val (state, event) = use(viewModel = viewModel)

    LaunchedEffect(key1 = market) {
        event.invoke(MarketDetailContract.Event.SetMarket(market = market))
    }

    LaunchedEffect(key1 = Unit) {
        onProvideBaseViewModel(viewModel)
    }

    MarketDetailScreen(
        marketDetailState = state,
        onFavoriteClick = {
            event.invoke(MarketDetailContract.Event.OnFavoriteClick(market = it))
        },
    )
}

@Composable
private fun MarketDetailScreen(
    marketDetailState: MarketDetailContract.State,
    onFavoriteClick: (market: Market?) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
//        WebView(
//            state = webViewState,
//            modifier = Modifier.fillMaxSize(),
//            captureBackPresses = false,
//        )

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {},
            backgroundColor = Color.White,
        ) {
            FavoriteIcon(isFavorite = marketDetailState.market?.isFavorite ?: false) {
                onFavoriteClick(marketDetailState.market)
            }
        }
    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ThemePreviews
@Composable
private fun MarketDetailScreenPrev(
    @PreviewParameter(MarketDetailStateProvider::class)
    marketDetailState: MarketDetailContract.State
) {
    ComposeNewsTheme {
        Scaffold {
            MarketDetailScreen(marketDetailState = marketDetailState, onFavoriteClick = {})
        }
    }
}