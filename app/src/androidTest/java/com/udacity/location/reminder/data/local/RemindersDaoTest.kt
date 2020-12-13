package com.udacity.location.reminder.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.location.reminder.data.dto.ReminderEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import org.junit.Assert.assertNull
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RemindersDaoTest {

    private lateinit var database: RemindersDatabase
    private lateinit var reminderEntity: ReminderEntity

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

        reminderEntity =
            ReminderEntity("Title", "description", "Googleplex", 40.7536, -73.9831, 100)
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun insertAndGetById() = runBlockingTest {
        database.reminderDao().saveReminder(reminderEntity)

        val response = database.reminderDao().getReminderById(reminderEntity.id.toString())

        assertThat(response as ReminderEntity, CoreMatchers.notNullValue())
        assertThat(response.id, `is`(reminderEntity.id))
        assertThat(response.title, `is`(reminderEntity.title))
        assertThat(response.description, `is`(reminderEntity.description))
        assertThat(response.location, `is`(reminderEntity.location))
        assertThat(response.latitude, `is`(reminderEntity.latitude))
        assertThat(response.longitude, `is`(reminderEntity.longitude))
    }

    @Test
    fun validateNoData() = runBlockingTest {
        val response = database.reminderDao().getReminders()
        assertThat(response.size, `is`(0))
    }

    @Test
    fun validateInsertReminder() = runBlockingTest {
        database.reminderDao().saveReminder(reminderEntity)

        val response = database.reminderDao().getReminders()

        assertThat(response.size, `is`(1))
    }

    @Test
    fun validateAndNotGetReminderById() = runBlockingTest {
        database.reminderDao().saveReminder(reminderEntity)

        val response = database.reminderDao().getReminderById("999")
        assertNull(response)
    }

}