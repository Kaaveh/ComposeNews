package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.repository.MarketRepository
import javax.inject.Inject

class SyncMarketsUseCase @Inject constructor(
    private val repository: MarketRepository
) {

    suspend operator fun invoke(): Boolean = repository.syncMarketList()

}