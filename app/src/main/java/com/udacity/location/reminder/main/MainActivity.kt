package com.udacity.location.reminder.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.udacity.location.reminder.R
import com.udacity.location.reminder.common.GeofenceActivity
import com.udacity.location.reminder.databinding.ActivityMainBinding
import com.udacity.location.reminder.util.notification.createChannel

class MainActivity : GeofenceActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun viewParent() = binding.contentMain.linearParent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        createChannel(this)
    }

}