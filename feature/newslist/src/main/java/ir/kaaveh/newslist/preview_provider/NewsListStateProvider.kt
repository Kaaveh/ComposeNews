package ir.kaaveh.newslist.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.kaaveh.domain.model_provider.newsList
import ir.kaaveh.newslist.NewsListContract

class NewsListStateProvider : PreviewParameterProvider<NewsListContract.State> {
    override val values: Sequence<NewsListContract.State> = sequenceOf(
        NewsListContract.State(
            news = newsList,
        ),
    )
}