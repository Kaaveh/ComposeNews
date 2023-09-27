package ir.composenews.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Market(
    val id: String,
    val name: String,
    val currentPrice: Double,
    val imageUrl: String,
    val isFavorite: Boolean = false,
) : Parcelable
