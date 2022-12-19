package ir.kaaveh.favoritenews.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.kaaveh.domain.model_provider.newsList
import ir.kaaveh.favoritenews.FavoriteNewsState

class FavoriteNewsStateProvider: PreviewParameterProvider<FavoriteNewsState> {
    override val values: Sequence<FavoriteNewsState> = sequenceOf(
        FavoriteNewsState(
            isLoading = false,
            news = newsList,
            error = ""
        ),
        FavoriteNewsState(
            isLoading = true,
            news = newsList,
            error = ""
        ),
        FavoriteNewsState(
            isLoading = false,
            news = newsList,
            error = "An Error Occurred!"
        ),
    )
}