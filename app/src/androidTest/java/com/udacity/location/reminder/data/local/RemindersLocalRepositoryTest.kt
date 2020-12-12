package com.udacity.location.reminder.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class RemindersLocalRepositoryTest {

    private lateinit var remindersLocalRepository: RemindersLocalRepository
    private lateinit var database: RemindersDatabase
    private lateinit var reminderEntity: ReminderEntity

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        remindersLocalRepository =
            RemindersLocalRepository(
                database.reminderDao(),
                Dispatchers.Main
            )

        reminderEntity =
            ReminderEntity("Title", "description", "Googleplex", 40.7536, -73.9831, 100)
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun saveAndGetReminderById() = runBlocking {

        remindersLocalRepository.saveReminder(reminderEntity)

        val response = remindersLocalRepository.getReminder(reminderEntity.id.toString())

        response as ResultType.Success
        assertThat(response.data.id, `is`(reminderEntity.id))
        assertThat(response.data.title, `is`(reminderEntity.title))
        assertThat(response.data.description, `is`(reminderEntity.description))
        assertThat(response.data.location, `is`(reminderEntity.location))
        assertThat(response.data.latitude, `is`(reminderEntity.latitude))
        assertThat(response.data.longitude, `is`(reminderEntity.longitude))
    }

    @Test
    fun validateNoData() = runBlocking {

        val response = remindersLocalRepository.getReminders()

        response as ResultType.Success
        assertThat(response.data.size, `is`(0))
    }

    @Test
    fun validateInsertReminder() = runBlocking {

        remindersLocalRepository.saveReminder(reminderEntity)

        val response = remindersLocalRepository.getReminders()

        response as ResultType.Success
        assertThat(response.data.size, `is`(1))
    }

}