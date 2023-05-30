package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.Market
import ir.kaaveh.domain.repository.MarketsRepository
import javax.inject.Inject

class ToggleFavoriteMarketsUseCase @Inject constructor(
    private val repository: MarketsRepository,
) {

    suspend operator fun invoke(news: Market) =
        repository.toggleFavoriteMarkets(news)

}