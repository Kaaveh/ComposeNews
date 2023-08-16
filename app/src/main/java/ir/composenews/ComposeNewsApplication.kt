package ir.composenews

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ir.composenews.sync.Sync

@HiltAndroidApp
class ComposeNewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Sync.init(this)
    }

}