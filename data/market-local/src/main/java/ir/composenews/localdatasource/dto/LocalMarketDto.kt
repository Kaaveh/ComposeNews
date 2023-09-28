package ir.composenews.localdatasource.dto

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "market_list", primaryKeys = ["id"])
data class LocalMarketDto(
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val imageUrl: String,
    @ColumnInfo(defaultValue = "false") val isFavorite: Boolean,
)
