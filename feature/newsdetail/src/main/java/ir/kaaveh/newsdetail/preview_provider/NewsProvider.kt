package ir.kaaveh.newsdetail.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.model_provider.newsList

class NewsProvider: PreviewParameterProvider<News> {
    override val values: Sequence<News> = sequenceOf(
        newsList[0],
        newsList[1],
    )
}