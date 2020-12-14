package com.udacity.location.reminder.description

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.location.reminder.R
import com.udacity.location.reminder.list.ReminderDataItem
import com.udacity.location.reminder.util.Constant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class DescriptionActivityTest {

    lateinit var scenario: ActivityScenario<DescriptionActivity>
    private val reminderDataItem =
        ReminderDataItem("Title", "description", "Googleplex", 40.7536, -73.9831, 100)

    @Before
    fun setup() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), DescriptionActivity::class.java)
        intent.putExtra(Constant.DATA, reminderDataItem)
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun test() {
        onView(withId(R.id.title_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.title_text_view)).check(matches(withText(reminderDataItem.title)))
        onView(withId(R.id.description_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.description_text_view)).check(matches(withText(reminderDataItem.description)))
    }

}