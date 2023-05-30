package ir.kaaveh.localdatasource.dto

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "markets", primaryKeys = ["id"])
data class LocalMarketsDto(
    val id: String,
    val name: String,
    val currentPrice: Double,
    val imageUrl: String,
    @ColumnInfo(defaultValue = "false") val isFavorite: Boolean,
)