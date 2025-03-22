package net.yyhis.flavormap.android

import android.app.Application
import de.hdodenhof.circleimageview.BuildConfig
import timber.log.Timber

class ApplicationInitializer  : Application() {

    override fun onCreate() {
        super.onCreate()

        // init timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}