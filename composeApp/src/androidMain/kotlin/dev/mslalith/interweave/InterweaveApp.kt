package dev.mslalith.interweave

import android.app.Application
import dev.mslalith.interweave.di.AppModule
import org.koin.ksp.generated.defaultModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class InterweaveApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@InterweaveApp)
            defaultModule()
            modules(AppModule().module)
        }
    }
}