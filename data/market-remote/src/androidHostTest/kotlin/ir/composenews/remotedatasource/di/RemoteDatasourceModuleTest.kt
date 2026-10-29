@file:Suppress("ktlint:standard:class-signature")

package ir.composenews.remotedatasource.di

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import ir.composenews.base.di.dispatcherModule
import ir.composenews.remotedatasource.api.MarketsApi
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class RemoteDatasourceModuleTest : StringSpec({

    "Given the dispatchers and an app engine, When MarketsApi is resolved, Then it is created" {
        val engineModule = module { single<HttpClientEngine> { OkHttp.create() } }
        val koin = koinApplication { modules(remoteDatasourceModule, dispatcherModule, engineModule) }.koin

        koin.get<MarketsApi>().shouldBeInstanceOf<MarketsApi>()
    }
})
