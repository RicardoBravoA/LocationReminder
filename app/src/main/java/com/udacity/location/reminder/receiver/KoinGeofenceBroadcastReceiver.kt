package com.udacity.location.reminder.receiver

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.location.reminder.R
import com.udacity.location.reminder.data.ReminderDataSource
import com.udacity.location.reminder.data.dto.ReminderEntity
import com.udacity.location.reminder.data.dto.ResultType
import com.udacity.location.reminder.list.ReminderDataItem
import com.udacity.location.reminder.util.Constant
import com.udacity.location.reminder.util.geofence.errorMessage
import com.udacity.location.reminder.util.notification.sendNotification
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

class KoinGeofenceBroadcastReceiver : KoinComponent {

    private val TAG = "GeofenceReceiver"
    private val dataSource: ReminderDataSource by inject()

    fun onReceive(context: Context, intent: Intent) {

        runBlocking {
            Log.i("z- dataBroadCast", dataSource.getReminders().toString())
        }

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

                runBlocking {
                    var data: List<ReminderEntity>? = null
                    when (val reminders = dataSource.getReminders()) {
                        is ResultType.Success -> {
                            data = reminders.data
                        }
                        is ResultType.Error -> Log.i("z- error", "error getting data")
                    }

                    val foundIndex = data?.indexOfFirst {
                        it.id == fenceId
                    }

                    val reminderEntity = foundIndex?.let { data?.get(it) }

                    reminderEntity?.let {
                        val reminderDataItem = ReminderDataItem(
                            it.title,
                            it.description,
                            it.location,
                            it.latitude,
                            it.longitude
                        )

                        // Unknown Geofences aren't helpful to us
                        if (-1 == foundIndex) {
                            Log.e(TAG, "Unknown Geofence: Abort Mission")
                            return@runBlocking
                        }

                        val notificationManager = ContextCompat.getSystemService(
                            context,
                            NotificationManager::class.java
                        ) as NotificationManager

                        notificationManager.sendNotification(
                            context, reminderDataItem!!
                        )
                    }

                }
            }
        }
    }
}
