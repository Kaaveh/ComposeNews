package ir.kaaveh.newslist.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.kaaveh.domain.model_provider.newsList
import ir.kaaveh.newslist.NewsListState

class NewsListStateProvider : PreviewParameterProvider<NewsListState> {
    override val values: Sequence<NewsListState> = sequenceOf(
        NewsListState(
            isLoading = false,
            news = newsList,
            error = ""
        ),
        NewsListState(
            isLoading = true,
            news = newsList,
            error = ""
        ),
        NewsListState(
            isLoading = false,
            news = newsList,
            error = "An Error Occurred!"
        ),
    )
}