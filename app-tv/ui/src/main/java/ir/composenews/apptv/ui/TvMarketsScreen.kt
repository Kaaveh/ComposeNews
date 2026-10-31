@file:Suppress("MagicNumber", "MaxLineLength", "LongMethod", "LongParameterList", "UnusedPrivateMember", "ktlint:standard:function-naming")

package ir.composenews.apptv.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import ir.composenews.base.LoadableData
import ir.composenews.core.ui.use
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.EmptyFavoritesPane
import ir.composenews.designsystem.component.MarketLogo
import ir.composenews.designsystem.component.Sparkline
import ir.composenews.designsystem.component.TrendChange
import ir.composenews.designsystem.component.formatCompactUsd
import ir.composenews.designsystem.component.formatPrice
import ir.composenews.designsystem.component.formatUsdPrice
import ir.composenews.designsystem.theme.LightExtendedColors
import ir.composenews.designsystem.theme.extendedColors
import ir.composenews.designsystem.theme.tabularNums
import ir.composenews.designsystem.widget.LoadingView
import ir.composenews.marketdetail.MarketDetailContract
import ir.composenews.marketdetail.MarketDetailContract.ChartRange
import ir.composenews.marketdetail.MarketDetailViewModel
import ir.composenews.marketlist.FavoriteListContract
import ir.composenews.marketlist.FavoriteListViewModel
import ir.composenews.marketlist.MarketListContract
import ir.composenews.marketlist.MarketListViewModel
import ir.composenews.network.Errors
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale
import androidx.compose.material3.MaterialTheme as PhoneMaterialTheme

private val RangeCorner = CornerSize(10.dp)
private val PillCorner = CornerSize(50)
private val CARD_WIDTH = 150.dp
private val CARD_HEIGHT = 96.dp
private const val FOCUSED_CARD_SCALE = 1.08f

/** The TV overscan margin on the sides. */
private val SCREEN_EDGE = 48.dp

/** Room at the top for the app's tab row, drawn over this screen so it outlives tab switches. */
val TAB_ROW_SPACE = 52.dp

/** The phone's 220 dp illustration doesn't leave room for the text on a 540 dp tall screen. */
internal val TV_ILLUSTRATION = 120.dp

/** Where the chart starts fading in from the left: as drawn, fully opaque from 35 % of its width. */
private const val CHART_FADE_END = 0.35f
private const val CHART_WIDTH_FRACTION = 0.64f
private const val CHART_FILL_ALPHA = 0.3f

/** Tabs switch on focus: waiting this long before fetching skips the markets only passed on the way. */
private const val FETCH_DELAY_MS = 400L

/**
 * One TV tab, Markets or [favorites]: the featured market on top, its chart behind it, and every market as a card
 * below. Clicking a card features it; the first market is featured until then.
 */
@Composable
fun TvMarketsRoute(
    favorites: Boolean,
    onSelectTab: (favorites: Boolean) -> Unit,
) {
    val (marketsState, marketsEvent) = use(viewModel = koinViewModel<MarketListViewModel>())
    val (favoritesState, favoritesEvent) = use(viewModel = koinViewModel<FavoriteListViewModel>())
    LaunchedEffect(Unit) {
        marketsEvent(MarketListContract.Event.OnGetMarketList)
        favoritesEvent(FavoriteListContract.Event.OnGetFavoriteList)
    }
    var featuredMarket by rememberSaveable { mutableStateOf<String?>(null) }
    var featuredFavorite by rememberSaveable { mutableStateOf<String?>(null) }
    // Set at launch and by buttons that remove themselves; whatever the screen shows next takes focus.
    var focusContent by rememberSaveable { mutableStateOf(true) }
    // One call for both tabs, so a switch keeps the featured market, and the chart it loaded, composed.
    TvMarketsScreen(
        markets = if (favorites) favoritesState.favoriteList else marketsState.marketList,
        favorites = favorites,
        featuredId = if (favorites) featuredFavorite else featuredMarket,
        onFeature = { if (favorites) featuredFavorite = it else featuredMarket = it },
        focusContent = focusContent,
        onContentFocused = { focusContent = false },
        refocusContent = { focusContent = true },
        onBrowseMarkets = {
            focusContent = true
            onSelectTab(false)
        },
        error = { error ->
            if (!favorites) {
                TvErrorPane(
                    error = error,
                    retryInSeconds = marketsState.retryInSeconds,
                    onRefresh = {
                        focusContent = true
                        marketsEvent(MarketListContract.Event.OnRefresh)
                    },
                    onOpenFavorites = {
                        focusContent = true
                        onSelectTab(true)
                    },
                    takeFocus = focusContent,
                    onFocusTaken = { focusContent = false },
                )
            }
        },
    )
}

@Composable
private fun TvMarketsScreen(
    markets: LoadableData<PersistentList<MarketModel>>,
    favorites: Boolean,
    featuredId: String?,
    onFeature: (id: String) -> Unit,
    focusContent: Boolean,
    onContentFocused: () -> Unit,
    refocusContent: () -> Unit,
    onBrowseMarkets: () -> Unit,
    error: @Composable (Errors) -> Unit,
) {
    val list = markets.data
    // A removed favorite or a market that left the top 250 falls back to the first one.
    val featured = list?.firstOrNull { it.id == featuredId } ?: list?.firstOrNull()

    Box(modifier = Modifier.fillMaxSize()) {
        if (list != null && featured != null) {
            // Removing the last favorite takes Saved, which has focus, away with the hero.
            FeaturedMarket(market = featured, onFavoriteClick = { if (favorites && list.size == 1) refocusContent() }) {
                MarketCardRow(
                    markets = list,
                    title = if (favorites) "Favorites" else "All markets",
                    focusFirstCard = focusContent,
                    onFirstCardFocused = onContentFocused,
                    onClick = { onFeature(it.id) },
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(start = SCREEN_EDGE, top = 24.dp, end = SCREEN_EDGE)) {
                Spacer(modifier = Modifier.height(TAB_ROW_SPACE))
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    when {
                        markets is LoadableData.Error -> {
                            error(markets.error)
                        }

                        favorites && list != null -> {
                            val browse = remember { FocusRequester() }
                            RequestFocusWhen(takeFocus = focusContent, requester = browse, onFocusTaken = onContentFocused)
                            EmptyFavoritesPane(illustrationSize = TV_ILLUSTRATION) {
                                TvPaneButton(
                                    text = "Browse markets",
                                    icon = painterResource(R.drawable.ic_finance_mode),
                                    onClick = onBrowseMarkets,
                                    modifier = Modifier.focusRequester(browse),
                                )
                            }
                        }

                        else -> {
                            LoadingView(modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
        }
    }
}

/** The featured market's detail over its full-bleed chart, with [cards] at the bottom. */
@Composable
private fun FeaturedMarket(
    market: MarketModel,
    onFavoriteClick: () -> Unit,
    cards: @Composable () -> Unit,
) {
    val (state, event) = use(viewModel = koinViewModel<MarketDetailViewModel>())
    LaunchedEffect(market.id) {
        event(MarketDetailContract.Event.SetMarket(market = market))
        delay(FETCH_DELAY_MS)
        event(MarketDetailContract.Event.GetMarketChart(marketId = market.id))
        event(MarketDetailContract.Event.GetMarketDetail(marketId = market.id))
    }
    // The ViewModel still holds the previous market for the frame before SetMarket.
    val isCurrent = state.market.data?.id == market.id
    val range = if (isCurrent) state.chartRange else ChartRange.Day
    val chart = state.marketChart.data?.takeIf { isCurrent }
    val detail = state.marketDetail.data?.takeIf { it.id == market.id }
    // Until the 1D chart answers, its change is the 24h change the list already has.
    val change = state.rangeChange?.takeIf { isCurrent } ?: market.priceChangePercentage24h.takeIf { range == ChartRange.Day }

    Box(modifier = Modifier.fillMaxSize()) {
        chart?.prices?.let { prices ->
            Sparkline(
                points = prices.map { it.second },
                color = PhoneMaterialTheme.extendedColors.chartLine,
                strokeWidth = 3.dp,
                fillAlpha = CHART_FILL_ALPHA,
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 52.dp)
                        .fillMaxWidth(CHART_WIDTH_FRACTION)
                        .height(315.dp)
                        .fadeInFromStart(),
            )
        }
        // No start padding here: the card row scrolls to the screen edge, so each part adds its own.
        Column(modifier = Modifier.fillMaxSize().padding(top = 24.dp, bottom = 24.dp)) {
            Spacer(modifier = Modifier.height(TAB_ROW_SPACE))
            Column(
                modifier = Modifier.padding(start = SCREEN_EDGE, top = 32.dp).width(400.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MarketLogo(id = market.id, imageUrl = market.imageUrl, size = 48.dp, shared = false)
                    Column {
                        Text(text = market.name, style = MaterialTheme.typography.headlineMedium)
                        val rank = market.marketCapRank ?: detail?.marketCapRank
                        Text(
                            text = market.symbol.uppercase(Locale.US) + (rank?.let { " · Rank #$it" } ?: ""),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Text(text = formatUsdPrice(market.currentPrice), style = MaterialTheme.typography.displayLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (change != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TrendChange(change = change)
                            Text(
                                text = " " + range.label.lowercase(Locale.US),
                                style = MaterialTheme.typography.bodyMedium,
                                color =
                                    if (change >= 0) {
                                        PhoneMaterialTheme.extendedColors.trendUp
                                    } else {
                                        PhoneMaterialTheme.extendedColors.trendDown
                                    },
                            )
                        }
                    }
                    Stat(label = "Market Cap", value = detail?.marketData?.marketCapUSD?.let(::formatCompactUsd))
                    Stat(label = "High 24h", value = detail?.marketData?.high24hUSD?.let(::formatPrice))
                }
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    SavedButton(
                        isFavorite = market.isFavorite,
                        onClick = {
                            onFavoriteClick()
                            event(MarketDetailContract.Event.OnFavoriteClick(market = market))
                        },
                    )
                    RangeGroup(
                        selected = range,
                        onSelect = { event(MarketDetailContract.Event.OnChartRangeSelect(it)) },
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            cards()
        }
    }
}

/** Fades the content in from transparent at its start edge, so the chart sits behind the text without a seam. */
private fun Modifier.fadeInFromStart(): Modifier =
    graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen).drawWithContent {
        drawContent()
        drawRect(
            brush = Brush.horizontalGradient(0f to Color.Transparent, CHART_FADE_END to Color.Black),
            blendMode = BlendMode.DstIn,
        )
    }

@Composable
private fun Stat(
    label: String,
    value: String?,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value ?: "—",
            style = MaterialTheme.typography.bodyMedium.tabularNums(),
            fontWeight = FontWeight.SemiBold,
        )
    }
}

/** `Favorite` on primaryContainer, `Saved` on primary, read out like the phone's favorite toggle. */
@Composable
private fun SavedButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
) {
    val container = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    val content = if (isFavorite) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
    Button(
        onClick = onClick,
        colors =
            ButtonDefaults.colors(
                containerColor = container,
                contentColor = content,
                focusedContainerColor = container,
                focusedContentColor = content,
            ),
        border = ButtonDefaults.border(focusedBorder = FocusRing),
        modifier =
            Modifier.semantics {
                stateDescription = if (isFavorite) "In favorites" else "Not in favorites"
                onClick(label = if (isFavorite) "Remove from favorites" else "Add to favorites", action = null)
            },
    ) {
        Icon(
            painter = painterResource(if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite),
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize),
        )
        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
        Text(text = if (isFavorite) "Saved" else "Favorite")
    }
}

/** 1D to 1Y: the selected range is a pill, the others connected rectangles with pill outer corners. */
@Composable
private fun RangeGroup(
    selected: ChartRange,
    onSelect: (ChartRange) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        ChartRange.entries.forEachIndexed { index, range ->
            val isSelected = range == selected
            val start = if (index == 0) PillCorner else RangeCorner
            val end = if (index == ChartRange.entries.lastIndex) PillCorner else RangeCorner
            val shape = if (isSelected) CircleShape else RoundedCornerShape(start, end, end, start)
            val container =
                if (isSelected) {
                    MaterialTheme.colorScheme.inverseSurface
                } else {
                    PhoneMaterialTheme.colorScheme.surfaceContainerHighest
                }
            val content = if (isSelected) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface
            Button(
                onClick = { onSelect(range) },
                shape = ButtonDefaults.shape(shape = shape),
                colors =
                    ButtonDefaults.colors(
                        containerColor = container,
                        contentColor = content,
                        focusedContainerColor = container,
                        focusedContentColor = content,
                    ),
                border = ButtonDefaults.border(focusedBorder = FocusRing.copy(shape = shape)),
                contentPadding = PaddingValues(horizontal = 18.dp),
                modifier = Modifier.semantics { if (isSelected) stateDescription = "Selected" },
            ) {
                Text(text = range.shortLabel)
            }
        }
    }
}

@Composable
private fun MarketCardRow(
    markets: List<MarketModel>,
    title: String,
    focusFirstCard: Boolean,
    onFirstCardFocused: () -> Unit,
    onClick: (MarketModel) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = SCREEN_EDGE))
        LazyRow(
            // Room for the focused card to scale up and show its ring.
            contentPadding = PaddingValues(start = SCREEN_EDGE, top = 10.dp, end = SCREEN_EDGE, bottom = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            itemsIndexed(items = markets, key = { _, market -> market.id }) { index, market ->
                if (index == 0) {
                    // Inside the item, so the requester is attached by the time it asks for focus.
                    val firstCard = remember { FocusRequester() }
                    RequestFocusWhen(takeFocus = focusFirstCard, requester = firstCard, onFocusTaken = onFirstCardFocused)
                    MarketCard(market = market, onClick = { onClick(market) }, modifier = Modifier.focusRequester(firstCard))
                } else {
                    MarketCard(market = market, onClick = { onClick(market) })
                }
            }
        }
    }
}

/** A market in the row. Focused, it scales to 1.08 with a white ring and turns light, with the light trend colors. */
@Composable
private fun MarketCard(
    market: MarketModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val shape = RoundedCornerShape(16.dp)
    val trendColors = if (focused) LightExtendedColors else PhoneMaterialTheme.extendedColors
    Card(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = CardDefaults.shape(shape = shape),
        colors =
            CardDefaults.colors(
                containerColor = PhoneMaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                focusedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
        scale = CardDefaults.scale(focusedScale = FOCUSED_CARD_SCALE),
        border = CardDefaults.border(focusedBorder = FocusRing.copy(shape = shape)),
        modifier = modifier.size(width = CARD_WIDTH, height = CARD_HEIGHT),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight().padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MarketLogo(id = market.id, imageUrl = market.imageUrl, size = 28.dp, shared = false)
                Text(
                    text = market.symbol.uppercase(Locale.US),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            Column {
                Text(
                    text = formatUsdPrice(market.currentPrice),
                    style = MaterialTheme.typography.titleSmall.tabularNums(),
                    maxLines = 1,
                )
                val change = market.priceChangePercentage24h
                TrendChange(change = change, color = if (change >= 0) trendColors.trendUp else trendColors.trendDown)
            }
        }
    }
}

internal val FocusRing = Border(border = BorderStroke(3.dp, Color.White), inset = (-3).dp)
