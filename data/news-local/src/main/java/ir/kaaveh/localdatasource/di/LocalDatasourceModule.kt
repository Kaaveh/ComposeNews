package ir.kaaveh.localdatasource.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.kaaveh.localdatasource.database.FavoriteNewsDao
import ir.kaaveh.localdatasource.database.FavoriteNewsDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDatasourceModule {

    @Singleton
    @Provides
    fun provideFavoriteNewsDatabase(app: Application): FavoriteNewsDatabase =
        Room.databaseBuilder(
            app,
            FavoriteNewsDatabase::class.java,
            FavoriteNewsDatabase.DATABASE_NAME,
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideFavoriteNewsDao(db: FavoriteNewsDatabase): FavoriteNewsDao =
        db.newsDao

}