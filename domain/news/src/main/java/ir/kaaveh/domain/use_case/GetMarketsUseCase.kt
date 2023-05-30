package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.Market
import ir.kaaveh.domain.repository.MarketsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMarketsUseCase @Inject constructor(
    private val repository: MarketsRepository,
) {

    suspend operator fun invoke(): Flow<List<Market>> {
        repository.syncMarkets()
        return repository.getMarkets()
    }

}