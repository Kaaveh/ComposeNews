@file:Suppress(
    "ktlint:standard:no-empty-first-line-in-class-body",
    "ktlint:standard:parameter-list-wrapping",
    "ktlint:standard:function-signature",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.localdatasource.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import ir.composenews.db.MarketDatabase
import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.localdatasource.database.MarketDaoImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val localDatasourceModule =
    module {

        single<MarketDatabase> {
            val driver: SqlDriver = AndroidSqliteDriver(MarketDatabase.Schema, get(), "MarketDatabase")
            MarketDatabase(driver)
        }

        singleOf(::MarketDaoImpl) bind MarketDao::class
    }
