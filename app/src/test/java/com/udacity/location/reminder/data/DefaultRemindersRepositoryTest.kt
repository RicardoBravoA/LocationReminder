package com.udacity.location.reminder.data

import com.udacity.location.reminder.MainCoroutineRule
import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType
import com.udacity.location.reminder.data.local.FakeRemindersLocalRepositoryTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultRemindersRepositoryTest {

    private val reminder1 =
        ReminderEntity("Title", "description", "Googleplex", 40.7536, -73.9831, 100)
    private val reminder2 =
        ReminderEntity("Title", "description", "Googleplex", 40.7536, -73.9831, 200)
    private val reminder3 =
        ReminderEntity("Title", "description", "Googleplex", 40.7536, -73.9831, 300)
    private val list = LinkedHashMap<String, ReminderEntity>()
    private val result = listOf(reminder1, reminder2, reminder3)

    // Class under test
    private lateinit var repository: FakeRemindersLocalRepositoryTest

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        list["100"] = reminder1
        list["200"] = reminder2
        list["300"] = reminder3
        repository = FakeRemindersLocalRepositoryTest(list)
    }

    @Test
    fun getTasks_requestsAllTasksFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        val tasks = repository.getReminders() as ResultType.Success
        assertThat(tasks.data, IsEqual(result))
    }

}