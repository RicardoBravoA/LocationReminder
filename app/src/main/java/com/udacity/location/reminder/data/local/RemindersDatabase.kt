package com.udacity.location.reminder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.location.reminder.data.dto.ReminderEntity

@Database(entities = [ReminderEntity::class], version = 1)
abstract class RemindersDatabase : RoomDatabase() {

    abstract fun reminderDao(): RemindersDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: RemindersDao

        fun createRemindersDao(context: Context): RemindersDao {
            synchronized(RemindersDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RemindersDatabase::class.java,
                        "locationReminders.db"
                    ).fallbackToDestructiveMigration()
                        .build().reminderDao()
                }
            }
            return INSTANCE
        }
    }

}