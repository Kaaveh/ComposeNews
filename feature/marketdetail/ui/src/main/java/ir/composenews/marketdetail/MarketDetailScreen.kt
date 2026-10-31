@file:Suppress(
    "UnusedPrivateMember",
    "LongMethod",
    "LongParameterList",
    "MagicNumber",
    "MaxLineLength",
    "ForbiddenComment",
    "ktlint"
)

package ir.composenews.marketdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import ir.composenews.core.ui.use
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.FavoriteFab
import ir.composenews.designsystem.component.MarketLogo
import ir.composenews.designsystem.component.TrendChange
import ir.composenews.designsystem.component.formatUsdPrice
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.theme.extendedColors
import ir.composenews.marketdetail.MarketDetailContract.ChartRange
import ir.composenews.marketdetail.MarketDetailContract.State
import ir.composenews.marketdetail.components.MarketChart
import ir.composenews.marketdetail.components.MarketData
import ir.composenews.marketdetail.components.label
import ir.composenews.marketdetail.preview_provider.MarketDetailStateProvider
import ir.composenews.uimarket.model.MarketModel
import java.util.Locale

/** [onBack] null hides the back button, for a detail pane shown next to its list. */
@Composable
fun MarketDetailRoute(
    market: MarketModel,
    onBack: (() -> Unit)? = null,
    viewModel: MarketDetailViewModel = koinViewModel(),
) {
    val (state, event) = use(viewModel = viewModel)
    val context = LocalContext.current

    LaunchedEffect(key1 = market) {
        event.invoke(MarketDetailContract.Event.SetMarket(market = market))
        // TODO: Move into viewModel?
        event.invoke(MarketDetailContract.Event.GetMarketChart(marketId = market.id))
        event.invoke(MarketDetailContract.Event.GetMarketDetail(marketId = market.id))
    }

    MarketDetailScreen(
        marketDetailState = state,
        onFavoriteClick = {
            event.invoke(MarketDetailContract.Event.OnFavoriteClick(market = it))
        },
        onBack = onBack,
        onShare = { context.shareMarket(it) },
        onChartRangeSelect = { event.invoke(MarketDetailContract.Event.OnChartRangeSelect(it)) },
    )
}

@Composable
internal fun MarketDetailScreen(
    marketDetailState: State,
    onFavoriteClick: (market: MarketModel) -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onShare: (market: MarketModel) -> Unit = {},
    onChartRangeSelect: (ChartRange) -> Unit = {},
) {
    val market = marketDetailState.market.data
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, bottom = 112.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "Back to markets")
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                if (market != null) {
                    IconButton(onClick = { onShare(market) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_share),
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            if (market != null) {
                MarketHeader(
                    market = market,
                    rank = market.marketCapRank ?: marketDetailState.marketDetail.data?.marketCapRank,
                    // Until the 1D chart answers, its change is the 24h change the list already has.
                    change = marketDetailState.rangeChange
                        ?: market.priceChangePercentage24h.takeIf { marketDetailState.chartRange == ChartRange.Day },
                    range = marketDetailState.chartRange,
                )
            }
            MarketChart(
                loadableData = marketDetailState.marketChart,
                range = marketDetailState.chartRange,
                onRangeSelect = onChartRangeSelect,
            )
            MarketData(
                loadableData = marketDetailState.marketDetail,
            )
        }
        if (market != null) {
            FavoriteFab(
                isFavorite = market.isFavorite,
                onClick = { onFavoriteClick(market) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            )
        }
    }
}

/** Logo, name, `BTC · Rank #1`, the price, and the change over [range] in a chip. */
@Composable
private fun MarketHeader(
    market: MarketModel,
    rank: Int?,
    change: Double?,
    range: ChartRange,
) {
    Column(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MarketLogo(id = market.id, imageUrl = market.imageUrl, size = 48.dp)
            Column {
                Text(text = market.name, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = listOfNotNull(market.symbol.uppercase(Locale.getDefault()), rank?.let { "Rank #$it" })
                        .joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp, lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Text(
            text = formatUsdPrice(market.currentPrice),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 10.dp),
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (change != null) {
                val up = change >= 0
                TrendChange(
                    change = change,
                    color = if (up) MaterialTheme.extendedColors.onTrendUpContainer else MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.labelLarge,
                    iconSize = 18.dp,
                    modifier = Modifier
                        .background(
                            color = if (up) MaterialTheme.extendedColors.trendUpContainer else MaterialTheme.colorScheme.errorContainer,
                            shape = CircleShape,
                        )
                        .padding(start = 8.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                )
            }
            Text(
                text = range.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun MarketDetailScreenPrev(
    @PreviewParameter(MarketDetailStateProvider::class) marketDetailState: State,
) {
    ComposeNewsTheme {
        Surface {
            MarketDetailScreen(
                marketDetailState = marketDetailState,
                onFavoriteClick = {},
                onBack = {},
            )
        }
    }
}
