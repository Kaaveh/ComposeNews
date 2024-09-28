@file:Suppress("MagicNumber", "MaxLineLength", "LongMethod")

package ir.composenews.appwatch.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import coil.compose.rememberAsyncImagePainter
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.shimmerEffect
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.theme.darkDownTrendRed
import ir.composenews.designsystem.theme.darkUptrendGreen
import ir.composenews.designsystem.theme.lightDownTrendRed
import ir.composenews.designsystem.theme.lightUptrendGreen
import java.util.Locale

@Composable
fun MarketItem(
    modifier: Modifier,
    name: String,
    symbol: String,
    urlToImage: String,
    price: String,
    priceChangePercentage24h: String,
    onItemClick: () -> Unit,
) {
    MarketItemCard(
        modifier = modifier,
        name = name,
        symbol = symbol,
        urlToImage = urlToImage,
        price = price,
        priceChangePercentage24h = priceChangePercentage24h,
        onItemClick = { onItemClick() },
    )
}

@Composable
private fun MarketItemCard(
    modifier: Modifier,
    name: String,
    symbol: String,
    urlToImage: String,
    price: String,
    priceChangePercentage24h: String,
    onItemClick: () -> Unit,
) {
    Card(
        modifier = modifier.wrapContentHeight(),
        onClick = { onItemClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = urlToImage),
                contentDescription = name,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                Text(
                    text = symbol.uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.caption3,
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.caption3,
                    maxLines = 1,
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                Text(
                    text = "$price $",
                    style = MaterialTheme.typography.caption3,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val color = when {
                        priceChangePercentage24h.contains("-") -> {
                            if (isSystemInDarkTheme()) darkDownTrendRed else lightDownTrendRed
                        }

                        else -> {
                            if (isSystemInDarkTheme()) darkUptrendGreen else lightUptrendGreen
                        }
                    }

                    ArrowIconUpOrDown(priceChangePercentage24h, tint = color)

                    Text(
                        text = "$priceChangePercentage24h %",
                        style = MaterialTheme.typography.caption3,
                        color = color,
                    )
                }
            }
        }
    }
}

@Composable
private fun ArrowIconUpOrDown(priceChangePercentage24h: String, tint: Color) {
    Icon(
        modifier = Modifier.size(size = 10.dp),
        painter = if (priceChangePercentage24h.contains("-")) {
            painterResource(id = R.drawable.baseline_arrow_downward_24)
        } else {
            painterResource(id = R.drawable.baseline_arrow_upward_24)
        },
        contentDescription = "",
        tint = tint,
    )
}

@Composable
private fun ShimmerMarketItem() {
    Card(
        modifier = Modifier.wrapContentHeight(),
        onClick = {},
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .shimmerEffect(),
            )
            Column(
                modifier = Modifier.weight(1F),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(10.dp)
                        .shimmerEffect(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(10.dp)
                        .shimmerEffect(),
                )
            }
        }
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun ShimmerMarketListItem() {
    ScalingLazyColumn(
        rememberResponsiveColumnState(
            contentPadding = ScalingLazyColumnDefaults.padding(
                first = ScalingLazyColumnDefaults.ItemType.Card,
                last = ScalingLazyColumnDefaults.ItemType.Card,
            ),
        ),
    ) {
        items(20) {
            ShimmerMarketItem()
        }
    }
}

@Preview(
    device = WearDevices.LARGE_ROUND,
    showSystemUi = true,
)
@Composable
private fun MarketItemPrev() {
    ComposeNewsTheme {
        MarketItem(
            modifier = Modifier,
            name = "Polkad",
            symbol = "NEARF",
            urlToImage = "",
            price = "100000",
            priceChangePercentage24h = "100000",
            onItemClick = {},
        )
    }
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true)
@Composable
private fun ShimmerMarketItemPrev() {
    ComposeNewsTheme {
        ShimmerMarketItem()
    }
}
