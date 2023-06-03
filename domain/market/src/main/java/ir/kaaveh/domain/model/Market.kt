package ir.kaaveh.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Market(
    val id: String,
    val name: String,
    val currentPrice: Double,
    val imageUrl: String,
    var isFavorite: Boolean = false,
): Parcelable