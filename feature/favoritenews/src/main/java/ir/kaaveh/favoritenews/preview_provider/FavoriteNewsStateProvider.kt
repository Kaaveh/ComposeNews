package ir.kaaveh.favoritenews.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.kaaveh.domain.model_provider.newsList
import ir.kaaveh.favoritenews.FavoriteNewsContract

class FavoriteNewsStateProvider: PreviewParameterProvider<FavoriteNewsContract.State> {
    override val values: Sequence<FavoriteNewsContract.State> = sequenceOf(
        FavoriteNewsContract.State(
            isLoading = false,
            news = newsList,
            error = ""
        ),
        FavoriteNewsContract.State(
            isLoading = true,
            news = newsList,
            error = ""
        ),
        FavoriteNewsContract.State(
            isLoading = false,
            news = newsList,
            error = "An Error Occurred!"
        ),
    )
}