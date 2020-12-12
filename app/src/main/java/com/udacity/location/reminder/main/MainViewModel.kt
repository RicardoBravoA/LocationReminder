package com.udacity.location.reminder.main

import android.app.Application
import androidx.lifecycle.map
import com.udacity.location.reminder.base.BaseViewModel

class MainViewModel(app: Application) : BaseViewModel(app) {

    val authenticationState = FirebaseUserLiveData().map { user ->
        user?.let {
            AuthenticationState.AUTHENTICATED
        } ?: AuthenticationState.UNAUTHENTICATED
    }

}