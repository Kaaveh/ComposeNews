@file:Suppress("PackageNaming", "PackageName", "ktlint")

package ir.composenews.domain.use_case

import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import kotlinx.coroutines.flow.Flow

open class GetMarketChartUseCase(
    private val repository: MarketRepository,
) {
    open operator fun invoke(id: String): Flow<Resource<MarketChart, Errors>> =
        repository.fetchChart(id = id)
}
