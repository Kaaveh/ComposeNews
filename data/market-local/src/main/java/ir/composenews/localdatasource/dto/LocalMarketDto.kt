package ir.composenews.localdatasource.dto

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "market_list", primaryKeys = ["id"])
data class LocalMarketDto(
    val id: String,
    val name: String,
    val currentPrice: Double,
    val imageUrl: String,
    @ColumnInfo(defaultValue = "false") val isFavorite: Boolean,
)
