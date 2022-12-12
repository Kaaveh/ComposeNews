package ir.kaaveh.newslist.component

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
import ir.kaaveh.designsystem.component.FavoriteIcon
import ir.kaaveh.domain.model.News

@Composable
fun NewsListItem(
    news: News,
    onItemClick: (News) -> Unit,
    onFavoriteClick: (News) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .clickable { onItemClick(news) },
        horizontalAlignment = Alignment.End,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = news.urlToImage),
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
                    text = news.title,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = news.description,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = news.source,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = news.publishedAt,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        FavoriteIcon(isFavorite = news.isFavorite) {
            onFavoriteClick(news)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
    }

}