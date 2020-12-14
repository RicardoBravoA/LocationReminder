package com.udacity.location.reminder.data

import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType

class FakeReminderDataSource : ReminderDataSource {

    var data: LinkedHashMap<String, ReminderEntity> = LinkedHashMap()

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getReminders(): ResultType<List<ReminderEntity>> {
        if (shouldReturnError) {
            return ResultType.Error("Test exception")
        }
        return ResultType.Success(data.values.toList())
    }

    override suspend fun saveReminder(reminder: ReminderEntity) {
        data[reminder.id.toString()] = reminder
    }

    override suspend fun getReminder(id: String): ResultType<ReminderEntity> {
        if (shouldReturnError) {
            return ResultType.Error("Test exception")
        }
        data[id]?.let {
            return ResultType.Success(it)
        }
        return ResultType.Error("Could not find task"))
    }

    override suspend fun deleteAllReminders() {
        data.clear()
    }

}