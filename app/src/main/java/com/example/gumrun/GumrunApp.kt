package com.example.gumrun

import android.app.Application
import com.example.auth.data.di.authDataModule
import com.example.auth.presetation.di.authViewModule
import com.example.core.data.di.coreDataModule
import com.example.gumrun.di.appModule
import com.example.run.presentation.run_overview.di.runViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber


class GumrunApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@GumrunApp)
            modules(
                authDataModule,
                authViewModule,
                appModule,
                coreDataModule,
                runViewModelModule
            )

        }
    }

}