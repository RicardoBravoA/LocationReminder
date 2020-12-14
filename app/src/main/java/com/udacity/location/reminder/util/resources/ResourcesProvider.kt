package com.udacity.location.reminder.util.resources

import android.content.Context
import com.udacity.location.reminder.R

open class ResourcesProvider(private val context: Context) : ResourcesInterface {

    override fun reminderTitleError() = context.getString(R.string.reminder_title_error)

    override fun reminderDescriptionError() = context.getString(R.string.reminder_description_error)

    override fun reminderLocationError() =
        context.getString(R.string.reminder_select_location_error)

}