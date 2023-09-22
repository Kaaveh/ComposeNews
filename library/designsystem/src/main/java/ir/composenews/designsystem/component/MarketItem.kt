@file:Suppress("MagicNumber")

package ir.composenews.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketItem(
    modifier: Modifier,
    name: String,
    urlToImage: String,
    price: String,
    isFavorite: Boolean,
    showFavoriteList: Boolean,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val positionalThreshold = with(LocalDensity.current) { 170.dp.toPx() }
    var show by remember {
        mutableStateOf(true)
    }
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                show = false
                true
            } else {
                false
            }
        },
        positionalThreshold = { positionalThreshold },
    )

    if (showFavoriteList) {
        AnimatedVisibility(visible = show, exit = fadeOut(spring())) {
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    DismissBackgroundSwipe(
                        modifier = Modifier,
                        dismissState = dismissState,
                    )
                },
                dismissContent = {
                    MarketItemCard(
                        modifier = modifier,
                        name = name,
                        urlToImage = urlToImage,
                        price = price,
                        isFavorite = isFavorite,
                        onItemClick = { onItemClick() },
                        onFavoriteClick = { onFavoriteClick() },
                    )
                },
            )
        }
    } else {
        MarketItemCard(
            modifier = modifier,
            name = name,
            urlToImage = urlToImage,
            price = price,
            isFavorite = isFavorite,
            onItemClick = { onItemClick() },
            onFavoriteClick = { onFavoriteClick() },
        )
    }

    LaunchedEffect(show) {
        if (show.not()) {
            delay(300)
            onFavoriteClick()
        }
    }
}

@Composable
private fun MarketItemCard(
    modifier: Modifier,
    name: String,
    urlToImage: String,
    price: String,
    isFavorite: Boolean,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .wrapContentHeight()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onItemClick() },
        shape = MaterialTheme.shapes.large,
        colors = if (isSystemInDarkTheme().not()) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        } else {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        },
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = urlToImage),
                contentDescription = name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
            )
            Column(
                modifier = Modifier.weight(1F),
            ) {
                Text(text = name, style = MaterialTheme.typography.headlineSmall)
                Text(text = "$price $", style = MaterialTheme.typography.bodyLarge)
            }
            Column {
                FavoriteIcon(isFavorite = isFavorite) {
                    onFavoriteClick()
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun MarketItemPrev() {
    ComposeNewsTheme {
        Surface {
            MarketItem(
                modifier = Modifier,
                name = "Title",
                urlToImage = "",
                price = "100000",
                isFavorite = false,
                showFavoriteList = false,
                onItemClick = {},
                onFavoriteClick = {},
            )
        }
    }
}

@Composable
private fun ShimmerMarketItem() {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp)),
        shape = MaterialTheme.shapes.large,
        colors = if (isSystemInDarkTheme().not()) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        } else {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        },
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .shimmerEffect(),
            )
            Column(
                modifier = Modifier.weight(1F),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(20.dp)
                        .shimmerEffect(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                        .shimmerEffect(),
                )
            }
        }
    }
}

@Composable
fun ShimmerMarketListItem() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(20) {
            ShimmerMarketItem()
        }
    }
}

@ThemePreviews
@Composable
private fun ShimmerMarketItemPrev() {
    ComposeNewsTheme {
        Surface {
            ShimmerMarketItem()
        }
    }
}
