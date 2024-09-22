package ir.composenews

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ComposeNewsWearApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}