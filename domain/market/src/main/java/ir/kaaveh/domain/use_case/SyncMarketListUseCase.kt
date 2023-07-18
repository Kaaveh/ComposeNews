package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.repository.MarketRepository
import javax.inject.Inject

class SyncMarketListUseCase @Inject constructor(
    private val repository: MarketRepository
) {

    suspend operator fun invoke() = repository.syncMarketList()

}