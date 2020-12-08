package com.udacity.location.reminder.list

import com.udacity.location.reminder.R
import com.udacity.location.reminder.base.BaseRecyclerViewAdapter

class RemindersListAdapter(callBack: (selectedReminder: ReminderDataItem) -> Unit) :
    BaseRecyclerViewAdapter<ReminderDataItem>(callBack) {
    override fun getLayoutRes(viewType: Int) = R.layout.item_reminder
}