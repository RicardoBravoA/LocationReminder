package com.udacity.location.reminder.util

import java.util.concurrent.TimeUnit

internal object Constant {
    const val DATA = "data"
    const val GPS = 100
    const val REQUEST_CODE = 1000
    const val MAP_ZOOM = 15f

    val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.HOURS.toMillis(1)

    const val GEOFENCE_RADIUS_IN_METERS = 100f
    const val EXTRA_GEOFENCE_INDEX = "GEOFENCE_INDEX"

    const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
    const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
    const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
    const val LOCATION_PERMISSION_INDEX = 0
    const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
    internal const val ACTION_GEOFENCE_EVENT =
        "DescriptionActivity.locationReminder.action.ACTION_GEOFENCE_EVENT"

}