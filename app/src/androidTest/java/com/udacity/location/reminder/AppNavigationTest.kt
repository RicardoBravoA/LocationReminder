package com.udacity.location.reminder

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.location.reminder.data.ReminderDataSource
import com.udacity.location.reminder.util.DataBindingIdlingResource
import com.udacity.location.reminder.util.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
@LargeTest
class AppNavigationTest: KoinTest {

    private val reminderDataSource: ReminderDataSource by inject()

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun before() {
        startKoin{   modules(
            listOf(applicationModule)
        ) }
    }

    @After
    fun after() {
        stopKoin()
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }


}