package ir.composenews.localdatasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.composenews.localdatasource.dto.LocalMarketDto

@Database(
    entities = [LocalMarketDto::class],
    version = 3,
)
abstract class MarketsDatabase : RoomDatabase() {

    abstract val marketDao: MarketDao

    companion object {
        const val DATABASE_NAME = "market_db"
    }
}
