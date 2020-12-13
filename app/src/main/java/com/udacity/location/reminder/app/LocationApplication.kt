package com.udacity.location.reminder.app

import androidx.multidex.MultiDexApplication
import com.udacity.location.reminder.di.myModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LocationApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@LocationApplication)
            modules(listOf(myModule))
        }
    }
}