package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.repository.MarketsRepository
import javax.inject.Inject

class SyncMarketsUseCase @Inject constructor(
    private val repository: MarketsRepository
) {

    suspend operator fun invoke(): Boolean = repository.syncMarkets()

}