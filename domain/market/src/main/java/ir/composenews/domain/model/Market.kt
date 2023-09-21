package ir.composenews.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Market(
    val id: String,
    val name: String,
    val currentPrice: Double,
    val imageUrl: String,
    var isFavorite: Boolean = false,
) : Parcelable
