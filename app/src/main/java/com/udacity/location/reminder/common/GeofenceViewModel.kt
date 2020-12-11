package com.udacity.location.reminder.common

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.udacity.location.reminder.util.Constant

class GeofenceViewModel(state: SavedStateHandle) : ViewModel() {

    private val _geofenceIndex = state.getLiveData(Constant.GEOFENCE_INDEX_KEY, -1)
    private val _hintIndex = state.getLiveData(Constant.HINT_INDEX_KEY, 0)

    fun updateHint(currentIndex: Int) {
        _hintIndex.value = currentIndex + 1
    }

    fun geofenceActivated() {
        _geofenceIndex.value = _hintIndex.value
    }

    fun geofenceIsActive() = _geofenceIndex.value == _hintIndex.value
    fun nextGeofenceIndex() = _hintIndex.value ?: 0
}