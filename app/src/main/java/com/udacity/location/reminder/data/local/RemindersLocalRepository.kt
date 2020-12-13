package com.udacity.location.reminder.data.local

import com.udacity.location.reminder.data.ReminderDataSource
import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType
import com.udacity.location.reminder.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemindersLocalRepository(
    private val remindersDao: RemindersDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReminderDataSource {

    /**
     * Get the reminders list from the local db
     * @return Result the holds a Success with all the reminders or an Error object with the error message
     */
    override suspend fun getReminders(): ResultType<List<ReminderEntity>> {
        wrapEspressoIdlingResource {
            return withContext(ioDispatcher) {
                return@withContext try {
                    ResultType.Success(remindersDao.getReminders())
                } catch (ex: Exception) {
                    ResultType.Error(ex.localizedMessage)
                }
            }
        }
    }

    /**
     * Insert a reminder in the db.
     * @param reminder the reminder to be inserted
     */
    override suspend fun saveReminder(reminder: ReminderEntity) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                remindersDao.saveReminder(reminder)
            }
        }
    }

    /**
     * Get a reminder by its id
     * @param id to be used to get the reminder
     * @return Result the holds a Success object with the Reminder or an Error object with the error message
     */
    override suspend fun getReminder(id: String): ResultType<ReminderEntity> {
        wrapEspressoIdlingResource {
            return withContext(ioDispatcher) {
                try {
                    val reminder = remindersDao.getReminderById(id)
                    if (reminder != null) {
                        return@withContext ResultType.Success(reminder)
                    } else {
                        return@withContext ResultType.Error("Reminder not found!")
                    }
                } catch (e: Exception) {
                    return@withContext ResultType.Error(e.localizedMessage)
                }
            }
        }
    }

    /**
     * Deletes all the reminders in the db
     */
    override suspend fun deleteAllReminders() {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                remindersDao.deleteAllReminders()
            }
        }
    }
}