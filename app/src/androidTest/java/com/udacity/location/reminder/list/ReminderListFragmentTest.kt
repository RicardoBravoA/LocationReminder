package com.udacity.location.reminder.list

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.location.reminder.R
import com.udacity.location.reminder.data.ReminderDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import androidx.test.espresso.action.ViewActions.click
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class ReminderListFragmentTest : KoinTest {

    private val reminderDataSource: ReminderDataSource by inject()

    @Test
    fun clickAddButton_navigateToMapLocationFragment() {
        val scenario = launchFragmentInContainer<ReminderListFragment>(
            Bundle(),
            R.style.Theme_LocationReminder
        )
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(ViewMatchers.withId(R.id.add_button)).perform(click())

        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )
    }

}