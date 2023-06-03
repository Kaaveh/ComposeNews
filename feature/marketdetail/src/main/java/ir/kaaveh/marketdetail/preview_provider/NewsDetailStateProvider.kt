package ir.kaaveh.marketdetail.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.kaaveh.domain.model_provider.marketList
import ir.kaaveh.marketdetail.NewsDetailContract

class NewsDetailStateProvider : PreviewParameterProvider<NewsDetailContract.State> {
    override val values: Sequence<NewsDetailContract.State> = sequenceOf(
        NewsDetailContract.State(
            marketList[0],
        ),
    )
}