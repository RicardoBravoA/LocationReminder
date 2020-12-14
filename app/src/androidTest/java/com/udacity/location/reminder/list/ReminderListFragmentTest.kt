package com.udacity.location.reminder.list

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.location.reminder.R
import com.udacity.location.reminder.data.ReminderDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.click
import com.udacity.location.reminder.data.dto.ReminderEntity
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito.*
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class ReminderListFragmentTest : KoinTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val reminderDataSource: ReminderDataSource by inject()
    private val reminderEntity =
        ReminderEntity("Title", "description", "Googleplex", 40.7536, -73.9831, 100)
    private lateinit var navController: NavController

    @Before
    fun setup() {
        val scenario = launchFragmentInContainer<ReminderListFragment>(
            Bundle(),
            R.style.Theme_LocationReminder
        )
        navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
    }

    @Test
    fun clickAddButton_navigateToMapLocationFragment() {
        onView(ViewMatchers.withId(R.id.add_button)).perform(click())
        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )
    }

    @Test
    fun validateDataInRecyclerView() {
        runBlockingTest {
            reminderDataSource.saveReminder(reminderEntity)
        }

        onView(ViewMatchers.withId(R.id.reminder_recycler_view)).check(
            matches(
                hasChildCount(1)
            )
        )
    }

}