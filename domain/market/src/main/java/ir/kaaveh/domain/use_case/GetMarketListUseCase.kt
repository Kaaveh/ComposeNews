package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.Market
import ir.kaaveh.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMarketListUseCase @Inject constructor(
    private val repository: MarketRepository,
) {

    suspend operator fun invoke(): Flow<List<Market>> {
        repository.syncMarketList()
        return repository.getMarketList()
    }

}