package ir.kaaveh.localdatasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.kaaveh.localdatasource.dto.FavoriteNewsDto

@Database(
    entities = [FavoriteNewsDto::class],
    version = 1,
)
abstract class FavoriteNewsDatabase: RoomDatabase() {

    abstract val newsDao: FavoriteNewsDao

    companion object {
        const val DATABASE_NAME = "news_db"
    }
}