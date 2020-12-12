package com.udacity.location.reminder.list

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReminderDataItem(
    var title: String?,
    var description: String?,
    var location: String?,
    var latitude: Double?,
    var longitude: Double?,
    val id: Int = 0
) : Parcelable