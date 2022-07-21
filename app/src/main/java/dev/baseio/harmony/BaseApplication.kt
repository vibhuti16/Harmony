package dev.baseio.harmony

import android.app.Application
import dev.baseio.harmony.core.TimberFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        TimberFactory.setupOnDebug()
    }
}

