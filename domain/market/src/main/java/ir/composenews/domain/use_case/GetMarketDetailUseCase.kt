@file:Suppress("PackageNaming", "PackageName", "ktlint:standard:annotation")

package ir.composenews.domain.use_case

import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.ktor.Errors
import ir.composenews.ktor.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

open class GetMarketDetailUseCase @Inject constructor(
    private val repository: MarketRepository,
) {
    open operator fun invoke(id: String): Flow<Resource<MarketDetail, Errors>> = repository.fetchDetail(id = id)
}
