package ir.kaaveh.localdatasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.kaaveh.localdatasource.dto.LocalNewsDto

@Database(
    entities = [LocalNewsDto::class],
    version = 3,
)
abstract class NewsDatabase : RoomDatabase() {

    abstract val newsDao: NewsDao

    companion object {
        const val DATABASE_NAME = "news_db"
    }
}