package ir.kaaveh.composenews

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ir.kaaveh.sync.Sync

@HiltAndroidApp
class ComposeNewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Sync.init(this)
    }

}