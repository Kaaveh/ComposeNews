package ir.kaaveh.localdatasource.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.kaaveh.localdatasource.database.NewsDao
import ir.kaaveh.localdatasource.database.NewsDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDatasourceModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(app: Application): NewsDatabase =
        Room.databaseBuilder(
            app,
            NewsDatabase::class.java,
            NewsDatabase.DATABASE_NAME,
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideNewsDao(db: NewsDatabase): NewsDao =
        db.newsDao

}