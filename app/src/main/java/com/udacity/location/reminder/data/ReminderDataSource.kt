package com.udacity.location.reminder.data

import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType

interface ReminderDataSource {
    suspend fun getReminders(): ResultType<List<ReminderEntity>>
    suspend fun saveReminder(reminder: ReminderEntity)
    suspend fun getReminder(id: String): ResultType<ReminderEntity>
    suspend fun deleteAllReminders()
}