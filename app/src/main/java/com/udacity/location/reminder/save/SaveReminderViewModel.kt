package com.udacity.location.reminder.save

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.location.reminder.R
import com.udacity.location.reminder.base.BaseViewModel
import com.udacity.location.reminder.base.NavigationCommand
import com.udacity.location.reminder.data.ReminderDataSource
import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.list.ReminderDataItem
import com.udacity.location.reminder.util.SingleEvent
import kotlinx.coroutines.launch

class SaveReminderViewModel(val app: Application, val dataSource: ReminderDataSource) :
    BaseViewModel(app) {

    private val _reminderTitle = MutableLiveData<SingleEvent<String?>?>()
    val reminderTitle: LiveData<SingleEvent<String?>?>
        get() = _reminderTitle

    private val _reminderDescription = MutableLiveData<SingleEvent<String?>?>()
    val reminderDescription: LiveData<SingleEvent<String?>?>
        get() = _reminderDescription

    private val _selectedPOI = MutableLiveData<SingleEvent<PointOfInterest?>?>()
    val selectedPOI: LiveData<SingleEvent<PointOfInterest?>?>
        get() = _selectedPOI

    fun addSelectedPOI(poi: PointOfInterest?) {
        _selectedPOI.value = SingleEvent(poi)
    }

    /**
     * Clear the live data objects to start fresh next time the view model gets called
     */
    fun onClear() {
        _reminderTitle.value = null
        _reminderDescription.value = null
        _selectedPOI.value = null
    }

    /**
     * Validate the entered data then saves the reminder data to the DataSource
     */
    fun validate(title: String, description: String) {
        val poi = _selectedPOI.value?.getContentIfNotHandled()
        val reminderDataItem = ReminderDataItem(
            title,
            description,
            poi?.name,
            poi?.latLng?.latitude,
            poi?.latLng?.longitude
        )

        if (validateData(reminderDataItem)) {
            saveReminder(reminderDataItem)
        }
    }

    /**
     * Save the reminder to the data source
     */
    fun saveReminder(reminderData: ReminderDataItem) {
        showLoading.value = true
        viewModelScope.launch {
            dataSource.saveReminder(
                ReminderEntity(
                    reminderData.title,
                    reminderData.description,
                    reminderData.location,
                    reminderData.latitude,
                    reminderData.longitude,
                    reminderData.id
                )
            )
            showLoading.value = false
            showToast.value = app.getString(R.string.reminder_saved)
            navigationCommand.value = NavigationCommand.Back
        }
    }

    /**
     * Validate the entered data and show error to the user if there's any invalid data
     */
    fun validateData(reminderData: ReminderDataItem): Boolean {
        if (reminderData.title.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.reminder_title_error
            return false
        }

        if (reminderData.location.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.reminder_description_error
            return false
        }
        return true
    }
}