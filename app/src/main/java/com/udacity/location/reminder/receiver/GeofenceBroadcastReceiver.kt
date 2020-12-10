package com.udacity.location.reminder.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.location.reminder.R
import com.udacity.location.reminder.list.ReminderDataItem
import com.udacity.location.reminder.util.Constant
import com.udacity.location.reminder.util.geofence.errorMessage
import com.udacity.location.reminder.util.notification.sendNotification

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "GeofenceReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Constant.ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)

            if (geofencingEvent.hasError()) {
                val errorMessage = errorMessage(context, geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                return
            }

            if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.v(TAG, context.getString(R.string.geofence_entered))

                val fenceId = when {
                    geofencingEvent.triggeringGeofences.isNotEmpty() ->
                        geofencingEvent.triggeringGeofences[0].requestId
                    else -> {
                        Log.e(TAG, "No Geofence Trigger Found! Abort mission!")
                        return
                    }
                }

                val reminderDataItem: ReminderDataItem? = ReminderDataItem(
                    "Computer History Museum",
                    "Description",
                    "Computer History Museum",
                    37.414827,
                    -122.077320
                )

                // Unknown Geofences aren't helpful to us
                if (reminderDataItem == null) {
                    Log.e(TAG, "Unknown Geofence: Abort Mission")
                    return
                }

                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendNotification(
                    context, reminderDataItem
                )
            }
        }
    }
}
