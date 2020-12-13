package com.udacity.location.reminder.data

import androidx.lifecycle.MutableLiveData
import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType

class FakeRemindersLocalRepository : ReminderDataSource {

    var remindersList: MutableList<ReminderEntity> = mutableListOf()

    private val observableTasks = MutableLiveData<Result<List<ReminderEntity>>>()

    override suspend fun getReminders(): ResultType<List<ReminderEntity>> {
        return ResultType.Success(remindersList.toList())
    }

    override suspend fun saveReminder(reminder: ReminderEntity) {
        remindersList.add(reminder)
    }

    override suspend fun getReminder(id: String): ResultType<ReminderEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllReminders() {
        TODO("Not yet implemented")
    }

}