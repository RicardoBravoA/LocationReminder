package com.udacity.location.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val broadcastHelper: KoinGeofenceBroadcastReceiver by lazy { KoinGeofenceBroadcastReceiver() }

    override fun onReceive(context: Context, intent: Intent) {
        broadcastHelper.onReceive(context, intent)
    }

}