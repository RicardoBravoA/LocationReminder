package com.udacity.location.reminder.app

import androidx.multidex.MultiDexApplication
import com.udacity.location.reminder.data.ReminderDataSource
import com.udacity.location.reminder.data.local.RemindersDatabase
import com.udacity.location.reminder.data.local.RemindersLocalRepository
import com.udacity.location.reminder.list.RemindersListViewModel
import com.udacity.location.reminder.main.MainViewModel
import com.udacity.location.reminder.save.SaveReminderViewModel
import com.udacity.location.reminder.util.resources.ResourcesProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class LocationApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            viewModel {
                MainViewModel(get())
            }
            //Declare singleton definitions to be later injected using by inject()
            single {
                //This view model is declared singleton to be used across multiple fragments
                SaveReminderViewModel(
                    get(),
                    get() as ReminderDataSource,
                    ResourcesProvider(get())
                )
            }
            single {
                RemindersListViewModel(
                    get(),
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { RemindersDatabase.createRemindersDao(this@LocationApplication) }
        }

        startKoin {
            androidContext(this@LocationApplication)
            modules(listOf(myModule))
        }
    }
}