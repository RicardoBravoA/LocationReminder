package com.udacity.location.reminder.data.local

import com.udacity.location.reminder.data.ReminderDataSource
import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType

class FakeRemindersLocalRepository(private var remindersList: LinkedHashMap<String, ReminderEntity>) :
    ReminderDataSource {

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