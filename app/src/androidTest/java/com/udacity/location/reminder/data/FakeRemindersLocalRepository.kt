package com.udacity.location.reminder.data

import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType

class FakeRemindersLocalRepository: ReminderDataSource {

    override suspend fun getReminders(): ResultType<List<ReminderEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveReminder(reminder: ReminderEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getReminder(id: String): ResultType<ReminderEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllReminders() {
        TODO("Not yet implemented")
    }

}