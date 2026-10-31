@file:Suppress("MagicNumber", "MaxLineLength", "LongParameterList", "LongMethod", "UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ir.composenews.designsystem.R
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.theme.extendedColors
import ir.composenews.designsystem.theme.tabularNums
import java.util.Locale
import kotlin.math.abs

/**
 * One market in a list: logo, symbol and name, 7-day sparkline, price, 24h change and the favorite toggle.
 * [shape] lets a grouped list give the first, middle and last rows their own corners.
 */
@Composable
fun MarketRow(
    id: String,
    name: String,
    symbol: String,
    imageUrl: String,
    price: String,
    change: Double,
    sparkline: List<Double>,
    isTrendingUp7d: Boolean?,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable(onClick = onClick)
            .padding(start = 12.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MarketLogo(id = id, imageUrl = imageUrl, size = 44.dp)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = symbol.uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp, lineHeight = 18.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (isTrendingUp7d != null) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clearAndSetSemantics {
                        contentDescription = if (isTrendingUp7d) "7-day trend up" else "7-day trend down"
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                val extendedColors = MaterialTheme.extendedColors
                Sparkline(
                    points = sparkline,
                    color = if (isTrendingUp7d) extendedColors.trendUp else extendedColors.trendDown,
                    modifier = Modifier.size(52.dp, 22.dp),
                )
                Text(
                    text = "7d",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, lineHeight = 12.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = price,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(600),
                ).tabularNums(),
                maxLines = 1,
            )
            TrendChange(change = change)
        }
        IconToggleButton(
            checked = isFavorite,
            onCheckedChange = { onFavoriteClick() },
        ) {
            Icon(
                painter = painterResource(if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite),
                contentDescription = if (isFavorite) "Remove $name from favorites" else "Add $name to favorites",
                tint = if (isFavorite) MaterialTheme.extendedColors.favoriteOn else MaterialTheme.extendedColors.favoriteOff,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

/** The coin logo, shared with the detail screen's when a shared-transition scope is provided. */
@Composable
internal fun MarketLogo(id: String, imageUrl: String, size: Dp) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .then(
                if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                    with(sharedTransitionScope) {
                        Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(key = "market_icon_$id"),
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                    }
                } else {
                    Modifier
                },
            )
            .size(size)
            .clip(CircleShape),
    )
}

/** An arrow and the percent change in the trend color, so the trend never relies on color alone. */
@Composable
internal fun TrendChange(
    change: Double,
    modifier: Modifier = Modifier,
    color: Color = if (change >= 0) {
        MaterialTheme.extendedColors.trendUp
    } else {
        MaterialTheme.extendedColors.trendDown
    },
    style: TextStyle = MaterialTheme.typography.bodySmall.copy(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight(600),
    ),
    iconSize: Dp = 16.dp,
) {
    val percent = String.format(Locale.US, "%.2f %%", abs(change))
    Row(
        modifier = modifier.semantics(mergeDescendants = true) {
            contentDescription = if (change >= 0) "Up $percent" else "Down $percent"
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(if (change >= 0) R.drawable.ic_arrow_upward else R.drawable.ic_arrow_downward),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(iconSize),
        )
        Text(text = percent, style = style.tabularNums(), color = color)
    }
}

@ThemePreviews
@Composable
private fun MarketRowPreview() {
    ComposeNewsTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                MarketRow(
                    id = "bitcoin",
                    name = "Bitcoin",
                    symbol = "btc",
                    imageUrl = "",
                    price = "$107,345.20",
                    change = 2.14,
                    sparkline = listOf(101.0, 103.5, 102.2, 105.0, 104.1, 107.3),
                    isTrendingUp7d = true,
                    isFavorite = true,
                    onClick = {},
                    onFavoriteClick = {},
                )
                MarketRow(
                    id = "ethereum",
                    name = "Ethereum",
                    symbol = "eth",
                    imageUrl = "",
                    price = "$3,812.06",
                    change = -1.37,
                    sparkline = listOf(4.0, 3.9, 3.95, 3.7, 3.8),
                    isTrendingUp7d = false,
                    isFavorite = false,
                    onClick = {},
                    onFavoriteClick = {},
                )
            }
        }
    }
}
