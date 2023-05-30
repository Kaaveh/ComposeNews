package ir.kaaveh.localdatasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.kaaveh.localdatasource.dto.LocalMarketsDto

@Database(
    entities = [LocalMarketsDto::class],
    version = 3,
)
abstract class MarketsDatabase : RoomDatabase() {

    abstract val marketDao: MarketDao

    companion object {
        const val DATABASE_NAME = "market_db"
    }
}