package com.udacity.location.reminder

import android.content.Context
import com.udacity.location.reminder.util.resources.ResourcesProvider

class FakeResourceProvider(private val context: Context) : ResourcesProvider(context) {

    override fun reminderTitleError() = "Reminder Title Error"

    override fun reminderDescriptionError() = "Reminder Description Error"

    override fun reminderLocationError() = "Reminder Location Error"

}