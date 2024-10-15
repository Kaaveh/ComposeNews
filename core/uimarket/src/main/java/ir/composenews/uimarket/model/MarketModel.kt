package ir.composenews.uimarket.model

import android.os.Parcel
import android.os.Parcelable

data class MarketModel(
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val imageUrl: String,
    val isFavorite: Boolean = false,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString().orEmpty(),
        parcel.readByte() != 0.toByte(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(symbol)
        parcel.writeDouble(currentPrice)
        parcel.writeDouble(priceChangePercentage24h)
        parcel.writeString(imageUrl)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MarketModel> {
        override fun createFromParcel(parcel: Parcel): MarketModel {
            return MarketModel(parcel)
        }

        override fun newArray(size: Int): Array<MarketModel?> {
            return arrayOfNulls(size)
        }
    }
}
