package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.Chart
import ir.kaaveh.domain.model.Resource
import ir.kaaveh.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMarketChartUseCase @Inject constructor(
    private val repository: MarketRepository,
) {

    operator fun invoke(id: String): Flow<Resource<Chart>> = repository.fetchChart(id = id)

}