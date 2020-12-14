package com.udacity.location.reminder.data

import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType

class FakeReminderDataSource : ReminderDataSource {

    var tasksServiceData: LinkedHashMap<String, ReminderEntity> = LinkedHashMap()

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getReminders(): ResultType<List<ReminderEntity>> {
        if (shouldReturnError) {
            return ResultType.Error("Test exception")
        }
        return ResultType.Success(tasksServiceData.values.toList())
    }

    override suspend fun saveReminder(reminder: ReminderEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getReminder(id: String): ResultType<ReminderEntity> {
        if (shouldReturnError) {
            return ResultType.Error("Test exception")
        }
        tasksServiceData[id]?.let {
            return ResultType.Success(it)
        }
        return ResultType.Error("Could not find task"))
    }

    override suspend fun deleteAllReminders() {
        TODO("Not yet implemented")
    }

}