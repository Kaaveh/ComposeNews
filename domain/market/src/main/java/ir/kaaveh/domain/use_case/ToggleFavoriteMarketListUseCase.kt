package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.Market
import ir.kaaveh.domain.repository.MarketRepository
import javax.inject.Inject

class ToggleFavoriteMarketListUseCase @Inject constructor(
    private val repository: MarketRepository,
) {

    suspend operator fun invoke(news: Market) =
        repository.toggleFavoriteMarket(news)

}