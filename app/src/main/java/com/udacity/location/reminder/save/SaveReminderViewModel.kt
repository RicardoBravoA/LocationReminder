package com.udacity.location.reminder.save

import android.app.Application
import android.text.TextUtils
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

    private val _validateTitle = MutableLiveData<Boolean>()
    val validateTitle: LiveData<Boolean>
        get() = _validateTitle

    private val _validateDescription = MutableLiveData<Boolean>()
    val validateDescription: LiveData<Boolean>
        get() = _validateDescription

    private val _validateData = MutableLiveData<SingleEvent<Boolean>>()
    val validateData: LiveData<SingleEvent<Boolean>>
        get() = _validateData

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

    fun validateData(title: String, description: String) {
        validateTitle(title)
        validateDescription(description)
        _validateData.value = SingleEvent(_validateTitle.value!! && _validateDescription.value!!)
    }

    private fun validateTitle(value: String) {
        _validateTitle.value = !TextUtils.isEmpty(value)
    }

    private fun validateDescription(value: String) {
        _validateDescription.value = !TextUtils.isEmpty(value)
    }

    // For TextWatcher
    fun validateTitleWatcher() {
        _validateTitle.value = true
    }

    fun validateDescriptionWatcher() {
        _validateDescription.value = true
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
    fun validateAndSaveReminder(reminderData: ReminderDataItem) {
        if (validateEnteredData(reminderData)) {
            saveReminder(reminderData)
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
    fun validateEnteredData(reminderData: ReminderDataItem): Boolean {
        if (reminderData.title.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_enter_title
            return false
        }

        if (reminderData.location.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_select_location
            return false
        }
        return true
    }
}