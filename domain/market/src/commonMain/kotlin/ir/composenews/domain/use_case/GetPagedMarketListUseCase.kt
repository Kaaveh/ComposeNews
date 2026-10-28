@file:Suppress("PackageNaming", "PackageName", "ktlint")

package ir.composenews.domain.use_case

import androidx.paging.PagingData
import ir.composenews.domain.model.Market
import ir.composenews.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow

class GetPagedMarketListUseCase(
    private val repository: MarketRepository,
) {
    operator fun invoke(): Flow<PagingData<Market>> = repository.getPagedMarketList()
}
