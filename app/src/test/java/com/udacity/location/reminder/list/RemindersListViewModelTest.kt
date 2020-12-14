package com.udacity.location.reminder.list

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.udacity.location.reminder.MainCoroutineRule
import com.udacity.location.reminder.data.FakeReminderDataSource
import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.hamcrest.CoreMatchers.`is`
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@ExperimentalCoroutinesApi
class RemindersListViewModelTest: KoinTest {

    private lateinit var viewModel: RemindersListViewModel
    private lateinit var dataSource: FakeReminderDataSource

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() = runBlockingTest {
        koinApplication {
            stopKoin()
        }
        dataSource = FakeReminderDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)

        val reminder1 = ReminderEntity("Title", "description", "Googleplex", 40.7536, -73.9831, 100)
        dataSource.saveReminder(reminder1)
    }

    @Test
    fun loadReminderTest() = runBlocking {
        viewModel.loadReminders()
        val value = viewModel.remindersList.getOrAwaitValue()
        assertThat(value, not(nullValue()))
    }

    @Test
    fun loadReminderAndShowNoDataTest() = runBlockingTest {
        dataSource.deleteAllReminders()
        dataSource.setReturnError(false)
        viewModel.loadReminders()

        assertThat(viewModel.showNoData.getOrAwaitValue(), `is`(true))
    }

    @Test
    fun loadReminderAndShowErrorSnackbarTest() {
        dataSource.setReturnError(true)
        viewModel.loadReminders()

        val snackbarText: String = viewModel.showSnackBar.getOrAwaitValue()
        assertThat(snackbarText, `is`("Test exception"))
    }

    @Test
    fun loadReminderAndValidateDataTest() = runBlockingTest {
        dataSource.deleteAllReminders()
        dataSource.setReturnError(false)

        dataSource.saveReminder(ReminderEntity("Title", "description", "Googleplex", 40.7536, -73.9831, 100))
        dataSource.saveReminder(ReminderEntity("Title", "description", "Googleplex", 40.7536, -73.9831, 200))
        dataSource.saveReminder(ReminderEntity("Title", "description", "Googleplex", 40.7536, -73.9831, 300))
        dataSource.saveReminder(ReminderEntity("Title", "description", "Googleplex", 40.7536, -73.9831, 400))

        viewModel.loadReminders()

        val value = viewModel.remindersList.getOrAwaitValue()
        assertThat(value, not(nullValue()))
        assertThat(value.size, `is`(4))
    }

}