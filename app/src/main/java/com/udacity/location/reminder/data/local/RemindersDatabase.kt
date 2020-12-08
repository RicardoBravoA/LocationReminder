package com.udacity.location.reminder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.udacity.location.reminder.data.dto.ReminderEntity

@Database(entities = [ReminderEntity::class], version = 1, exportSchema = false)
abstract class RemindersDatabase : RoomDatabase() {

    abstract fun reminderDao(): RemindersDao
}