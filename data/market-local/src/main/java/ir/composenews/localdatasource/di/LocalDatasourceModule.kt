package ir.composenews.localdatasource.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.localdatasource.database.MarketsDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDatasourceModule {

    @Singleton
    @Provides
    fun provideMarketsDatabase(app: Application): MarketsDatabase =
        Room.databaseBuilder(
            app,
            MarketsDatabase::class.java,
            MarketsDatabase.DATABASE_NAME,
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideMarketsDao(db: MarketsDatabase): MarketDao = db.marketDao
}
