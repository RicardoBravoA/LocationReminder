package com.udacity.location.reminder.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.location.reminder.data.dto.ReminderEntity

@Dao
interface RemindersDao {

    @Query("SELECT * FROM reminders")
    suspend fun getReminders(): List<ReminderEntity>

    @Query("SELECT * FROM reminders where entry_id = :reminderId")
    suspend fun getReminderById(reminderId: String): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminders")
    suspend fun deleteAllReminders()

}