@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.domain.use_case

import ir.composenews.domain.model.Market
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.domain.util.MarketListOrder
import ir.composenews.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMarketListUseCase @Inject constructor(
    private val repository: MarketRepository,
) {
    operator fun invoke(
        marketListOrder: MarketListOrder = MarketListOrder.Price(OrderType.Ascending),
    ): Flow<List<Market>> {
        return repository.getMarketList().map { marketList ->
            when (marketListOrder.orderType) {
                is OrderType.Ascending -> when (marketListOrder) {
                    is MarketListOrder.Name -> marketList.sortedBy { it.name }
                    is MarketListOrder.Price -> marketList.sortedBy { it.priceChangePercentage24h }
                    is MarketListOrder.Symbol -> marketList.sortedBy { it.symbol }
                }

                is OrderType.Descending -> when (marketListOrder) {
                    is MarketListOrder.Name -> marketList.sortedByDescending { it.name }
                    is MarketListOrder.Price -> marketList.sortedByDescending { it.priceChangePercentage24h }
                    is MarketListOrder.Symbol -> marketList.sortedByDescending { it.symbol }
                }
            }
        }
    }
}
