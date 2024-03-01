package com.rizrmdhn.storyapp

import android.app.Application
import com.rizrmdhn.core.di.databaseModule
import com.rizrmdhn.core.di.networkModule
import com.rizrmdhn.core.di.preferencesModule
import com.rizrmdhn.core.di.repositoryModule
import com.rizrmdhn.storyapp.di.useCaseModule
import com.rizrmdhn.storyapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    preferencesModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}