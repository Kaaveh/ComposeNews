package ir.kaaveh.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ir.kaaveh.designsystem.preview.ThemePreviews
import ir.kaaveh.designsystem.theme.ComposeNewsTheme

@Composable
fun NewsItem(
    title: String,
    urlToImage: String,
    description: String,
    source: String,
    publishedAt: String,
    isFavorite: Boolean,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .clickable { onItemClick() },
        horizontalAlignment = Alignment.End,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = urlToImage),
                contentDescription = null,
                modifier = Modifier
                    .width(160.dp)
                    .height(90.dp),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
            ) {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = description,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = source,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = publishedAt,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        FavoriteIcon(isFavorite = isFavorite) {
            onFavoriteClick()
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@ThemePreviews
@Composable
private fun NewsItemPrev() {
    ComposeNewsTheme {
        NewsItem(
            title = "Title",
            urlToImage = "",
            description = "This is a sample description.",
            source = "My Aunt",
            publishedAt = "2022/02.10",
            isFavorite = false,
            onItemClick = {},
            onFavoriteClick = {}
        )
    }
}