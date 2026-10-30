package ir.composenews.marketlist

import ir.composenews.marketlist.MarketListContract.Filter
import ir.composenews.marketlist.MarketListContract.Sort
import ir.composenews.uimarket.model.MarketModel
import ir.composenews.marketlist.FavoriteListContract.Sort as FavoriteSort

internal fun List<MarketModel>.filteredAndSorted(
    filter: Filter,
    sort: Sort,
): List<MarketModel> =
    when (filter) {
        Filter.All -> {
            when (sort) {
                Sort.MarketCap -> sortedBy { it.marketCapRank ?: Int.MAX_VALUE }
                Sort.Volume -> sortedByDescending { it.totalVolume ?: 0.0 }
            }
        }

        Filter.Gainers -> {
            filter { it.priceChangePercentage24h > 0 }.sortedByDescending { it.priceChangePercentage24h }
        }

        Filter.Losers -> {
            filter { it.priceChangePercentage24h < 0 }.sortedBy { it.priceChangePercentage24h }
        }
    }

internal fun List<MarketModel>.topMover(): MarketModel? =
    filter { it.priceChangePercentage24h > 0 }.maxByOrNull { it.priceChangePercentage24h }

internal fun List<MarketModel>.sortedFor(sort: FavoriteSort): List<MarketModel> =
    when (sort) {
        FavoriteSort.Name -> sortedBy { it.name.lowercase() }
        FavoriteSort.Price -> sortedByDescending { it.currentPrice }
        FavoriteSort.Change24h -> sortedByDescending { it.priceChangePercentage24h }
    }
