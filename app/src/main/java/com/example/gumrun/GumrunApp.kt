package com.example.gumrun

import android.app.Application
import com.example.auth.data.di.authDataModule
import com.example.auth.presetation.di.authViewModule
import com.example.core.data.di.coreDataModule
import com.example.gumrun.di.appModule
import com.example.run.location.di.LocationModule
import com.example.run.presentation.di.runPresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber


class GumrunApp : Application() {
    // To make all coroutine job is independent
    // If we don't do this if one coroutine fail, other coroutine will be affected
    val applicationScope = CoroutineScope(SupervisorJob())

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
                runPresentationModule,
                LocationModule
            )

        }
    }

}