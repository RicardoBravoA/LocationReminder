package com.udacity.location.reminder

import android.app.Activity
import android.widget.Toolbar
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.location.reminder.data.ReminderDataSource
import com.udacity.location.reminder.di.myModule
import com.udacity.location.reminder.main.MainActivity
import com.udacity.location.reminder.util.DataBindingIdlingResource
import com.udacity.location.reminder.util.EspressoIdlingResource
import com.udacity.location.reminder.util.monitorActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
@LargeTest
class AppNavigationTest : KoinTest {

    private val reminderDataSource: ReminderDataSource by inject()

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    /*@Before
    fun before() {
        startKoin {
            modules(
                listOf(myModule)
            )
        }
    }

    @After
    fun after() {
        stopKoin()
    }*/

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @Test
    fun loginScreen_clickOnLoginButton_OpensFirebaseUI() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)
        onView(ViewMatchers.withId(R.id.login_button)).perform(click())
        activityScenario.close()
    }


}

fun <T : Activity> ActivityScenario<T>.getToolbarNavigationContentDescription()
        : String {
    var description = ""
    onActivity {
        description =
            it.findViewById<Toolbar>(R.id.toolbar).navigationContentDescription as String
    }
    return description
}