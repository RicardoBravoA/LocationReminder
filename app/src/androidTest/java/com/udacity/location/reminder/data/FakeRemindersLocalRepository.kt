package com.udacity.location.reminder.data

import androidx.lifecycle.MutableLiveData
import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType

class FakeRemindersLocalRepository : ReminderDataSource {

    var remindersList: LinkedHashMap<String, ReminderEntity> = LinkedHashMap()

    private val observableTasks = MutableLiveData<Result<List<ReminderEntity>>>()

    override suspend fun getReminders(): ResultType<List<ReminderEntity>> {
        return ResultType.Success(remindersList.values.toList())
    }

    override suspend fun saveReminder(reminder: ReminderEntity) {
        remindersList[reminder.id.toString()] = reminder
    }

    override suspend fun getReminder(id: String): ResultType<ReminderEntity> {
        remindersList[id]?.let {
            return ResultType.Success(it)
        }
        return ResultType.Error("Could not find reminder")
    }

    override suspend fun deleteAllReminders() {
        remindersList.clear()
    }

}