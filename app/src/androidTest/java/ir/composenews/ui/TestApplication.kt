package ir.composenews.ui

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                testModule,
            )
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}
