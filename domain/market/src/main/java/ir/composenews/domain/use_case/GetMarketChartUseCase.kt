@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.domain.use_case

import ir.composenews.domain.model.Chart
import ir.composenews.domain.model.Resource
import ir.composenews.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMarketChartUseCase @Inject constructor(
    private val repository: MarketRepository,
) {
    operator fun invoke(id: String): Flow<Resource<Chart>> = repository.fetchChart(id = id)
}
