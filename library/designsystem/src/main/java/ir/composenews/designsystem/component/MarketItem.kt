package ir.composenews.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme

@Composable
fun MarketItem(
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
            .clickable { onItemClick() },
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = urlToImage),
                contentDescription = name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier.weight(1F)
            ) {
                Text(text = name, style = MaterialTheme.typography.h5)
                Text(text = "$price $", style = MaterialTheme.typography.body1)
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
                onItemClick = {},
                onFavoriteClick = {}
            )
        }
    }
}