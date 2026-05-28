package ir.composenews.ui

import android.app.Application
import org.koin.core.context.startKoin

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                testModule,
            )
        }
    }
}
