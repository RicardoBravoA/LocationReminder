package com.udacity.location.reminder.list

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.location.reminder.base.BaseViewModel
import com.udacity.location.reminder.data.ReminderDataSource
import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType
import kotlinx.coroutines.launch

class RemindersListViewModel(
    app: Application,
    private val dataSource: ReminderDataSource
) : BaseViewModel(app) {
    // list that holds the reminder data to be displayed on the UI
    val remindersList = MutableLiveData<List<ReminderDataItem>>()

    /**
     * Get all the reminders from the DataSource and add them to the remindersList to be shown on the UI,
     * or show error if any
     */
    fun loadReminders() {
        viewModelScope.launch {
            when (val result = dataSource.getReminders()) {
                is ResultType.Success<*> -> {
                    val dataList = ArrayList<ReminderDataItem>()
                    dataList.addAll((result.data as List<ReminderEntity>).map { reminder ->
                        //map the reminder data from the DB to the be ready to be displayed on the UI
                        ReminderDataItem(
                            reminder.title,
                            reminder.description,
                            reminder.location,
                            reminder.latitude,
                            reminder.longitude,
                            reminder.id
                        )
                    })
                    remindersList.value = dataList
                }
                is ResultType.Error -> {
                    showSnackBar.value = result.message!!
                }
            }
            invalidateShowNoData()
        }
        invalidateShowNoData()
    }

    /**
     * Inform the user that there's not any data if the remindersList is empty
     */

    private fun invalidateShowNoData() {
        showNoData.value = remindersList.value.isNullOrEmpty()
    }
}