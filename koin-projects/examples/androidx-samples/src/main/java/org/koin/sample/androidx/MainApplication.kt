package org.koin.sample.androidx

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.androidx.workmanager.KoinWorkerFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.sample.androidx.di.*

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KoinWorkerFactory.getInstance().init(this)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MainApplication)
            androidFileProperties()
            fragmentFactory()
            modules(appModule + mvpModule + mvvmModule + scopeModule + workerModule + workerScopedModule)
        }
    }
}