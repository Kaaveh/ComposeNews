package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.Market
import ir.kaaveh.domain.repository.MarketsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMarketsUseCase @Inject constructor(
    private val repository: MarketsRepository,
) {

    operator fun invoke(): Flow<List<Market>> = repository.getFavoriteMarkets()

}