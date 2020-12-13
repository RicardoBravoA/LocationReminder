package com.udacity.location.reminder.save

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.location.reminder.base.BaseViewModel
import com.udacity.location.reminder.base.NavigationCommand
import com.udacity.location.reminder.data.ReminderDataSource
import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.list.ReminderDataItem
import com.udacity.location.reminder.util.SingleEvent
import com.udacity.location.reminder.util.resources.ResourcesProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SaveReminderViewModel(
    val app: Application,
    private val dataSource: ReminderDataSource,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel(app) {

    private val _selectedPOI = MutableLiveData<PointOfInterest?>()
    val selectedPOI: LiveData<PointOfInterest?>
        get() = _selectedPOI

    private val _addGeofence = MutableLiveData<SingleEvent<Boolean>>()
    val addGeofence: LiveData<SingleEvent<Boolean>>
        get() = _addGeofence

    fun addSelectedPOI(poi: PointOfInterest?) {
        _selectedPOI.value = poi
    }

    /**
     * Clear the live data objects to start fresh next time the view model gets called
     */
    fun onClear() {
        _selectedPOI.value = null
    }

    /**
     * Validate the entered data then saves the reminder data to the DataSource
     */
    fun validate(title: String, description: String) {
        val poi = _selectedPOI.value
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
    private fun saveReminder(reminderData: ReminderDataItem) {
        showLoading.value = true
        val reminderEntity = ReminderEntity(
            reminderData.title,
            reminderData.description,
            reminderData.location,
            reminderData.latitude,
            reminderData.longitude,
            reminderData.id
        )
        viewModelScope.launch {
            try {
                coroutineScope {
                    dataSource.saveReminder(reminderEntity)
                    _addGeofence.postValue(SingleEvent(true))
                    showLoading.value = false
                    navigationCommand.value = NavigationCommand.Back
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Validate the entered data and show error to the user if there's any invalid data
     */
    private fun validateData(reminderData: ReminderDataItem): Boolean {
        if (reminderData.title.isNullOrEmpty()) {
            showSnackBar.value = resourcesProvider.reminderTitleError()
            return false
        }

        if (reminderData.description.isNullOrEmpty()) {
            showSnackBar.value = resourcesProvider.reminderDescriptionError()
            return false
        }

        if (reminderData.location.isNullOrEmpty() || reminderData.latitude == null || reminderData.longitude == null) {
            showSnackBar.value = resourcesProvider.reminderLocationError()
            return false
        }
        return true
    }
}