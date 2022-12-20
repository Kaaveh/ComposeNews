package ir.kaaveh.newsdetail.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.kaaveh.domain.model_provider.newsList
import ir.kaaveh.newsdetail.NewsDetailContract

class NewsDetailStateProvider: PreviewParameterProvider<NewsDetailContract.State> {
    override val values: Sequence<NewsDetailContract.State> = sequenceOf(
        NewsDetailContract.State(
            isLoading = false,
            newsList[0],
            error = ""
        ),
        NewsDetailContract.State(
            isLoading = true,
            newsList[0],
            error = ""
        ),
        NewsDetailContract.State(
            isLoading = false,
            newsList[0],
            error = "An Error Occurred!"
        ),
    )
}