@file:Suppress("MagicNumber", "MaxLineLength", "LongMethod", "LongParameterList", "UnusedPrivateMember", "ktlint:standard:function-naming")

package ir.composenews.appwatch.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.CardDefaults
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ListHeaderDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.tooling.preview.devices.WearDevices
import ir.composenews.appwatch.ui.ComposeNewsWearTheme
import ir.composenews.appwatch.ui.WearErrorContent
import ir.composenews.appwatch.ui.WearRefreshContent
import ir.composenews.appwatch.ui.WearTrendChange
import ir.composenews.appwatch.ui.wearScrollIndicator
import ir.composenews.base.LoadableData
import ir.composenews.base.errorViewMapper
import ir.composenews.core.ui.use
import ir.composenews.designsystem.component.MarketLogo
import ir.composenews.designsystem.component.formatUsdPrice
import ir.composenews.marketlist.MarketListContract
import ir.composenews.marketlist.MarketListViewModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale

@Composable
fun MarketListWearRoute(onNavigateToDetailScreen: (market: MarketModel) -> Unit) {
    val (state, event) = use(viewModel = koinViewModel<MarketListViewModel>())
    LaunchedEffect(Unit) {
        event(MarketListContract.Event.OnGetMarketList)
    }
    MarketListWearScreen(
        marketList = state.marketList,
        syncError = state.syncError?.let { errorViewMapper(it) },
        isRefreshing = state.isRefreshing,
        retryInSeconds = state.retryInSeconds,
        onRetry = { event(MarketListContract.Event.OnRefresh) },
        onNavigateToDetailScreen = onNavigateToDetailScreen,
    )
}

@Composable
private fun MarketListWearScreen(
    marketList: LoadableData<PersistentList<MarketModel>>,
    syncError: String?,
    isRefreshing: Boolean,
    retryInSeconds: Int,
    onRetry: () -> Unit,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
) {
    val columnState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()
    ScreenScaffold(scrollState = columnState, scrollIndicator = wearScrollIndicator(columnState)) { contentPadding ->
        TransformingLazyColumn(
            state = columnState,
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                ListHeader(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec)
                            .minimumVerticalContentPadding(ListHeaderDefaults.minimumTopListContentPadding),
                    contentColor = MaterialTheme.colorScheme.tertiary,
                    transformation = SurfaceTransformation(transformationSpec),
                ) {
                    Text(text = "Markets")
                }
            }
            when (marketList) {
                is LoadableData.Loaded -> {
                    items(items = marketList.data, key = { it.id }) { market ->
                        MarketCard(
                            market = market,
                            onClick = { onNavigateToDetailScreen(market) },
                            transformation = SurfaceTransformation(transformationSpec),
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .transformedHeight(this, transformationSpec)
                                    .minimumVerticalContentPadding(CardDefaults.minimumVerticalListContentPadding),
                        )
                    }
                    item {
                        WearRefreshContent(
                            syncError = syncError,
                            isRefreshing = isRefreshing,
                            retryInSeconds = retryInSeconds,
                            onRefresh = onRetry,
                            modifier =
                                Modifier
                                    .transformedHeight(this, transformationSpec)
                                    .minimumVerticalContentPadding(ButtonDefaults.minimumVerticalListContentPadding),
                        )
                    }
                }

                is LoadableData.Error -> {
                    item {
                        WearErrorContent(
                            message = errorViewMapper(marketList.error),
                            retryInSeconds = retryInSeconds,
                            onRetry = onRetry,
                        )
                    }
                }

                LoadableData.Loading, LoadableData.Initial -> {
                    item {
                        CircularProgressIndicator(
                            modifier =
                                Modifier
                                    .size(48.dp)
                                    .semantics { contentDescription = "Loading markets" },
                        )
                    }
                }
            }
        }
    }
}

/** A pill row: the logo, symbol and price, and the 24h change. */
@Composable
private fun MarketCard(
    market: MarketModel,
    onClick: () -> Unit,
    transformation: SurfaceTransformation,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(start = 8.dp, top = 8.dp, end = 14.dp, bottom = 8.dp),
        transformation = transformation,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MarketLogo(id = market.id, imageUrl = market.imageUrl, size = 36.dp, shared = false)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = market.symbol.uppercase(Locale.US),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Text(
                    text = formatUsdPrice(market.currentPrice),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
            WearTrendChange(change = market.priceChangePercentage24h)
        }
    }
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true)
@Composable
private fun MarketListWearScreenPreview() {
    ComposeNewsWearTheme {
        AppScaffold {
            MarketListWearScreen(
                marketList =
                    LoadableData.Loaded(
                        persistentListOf(
                            MarketModel("bitcoin", "Bitcoin", "btc", 67_412.80, 2.14, ""),
                            MarketModel("ethereum", "Ethereum", "eth", 3_284.55, 1.65, ""),
                            MarketModel("solana", "Solana", "sol", 152.08, -1.37, ""),
                        ),
                    ),
                syncError = null,
                isRefreshing = false,
                retryInSeconds = 0,
                onRetry = {},
                onNavigateToDetailScreen = {},
            )
        }
    }
}
