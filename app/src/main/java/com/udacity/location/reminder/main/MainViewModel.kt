package com.udacity.location.reminder.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.udacity.location.reminder.login.AuthenticationState
import com.udacity.location.reminder.login.FirebaseUserLiveData

class MainViewModel : ViewModel() {

    val authenticationState = FirebaseUserLiveData().map { user ->
        user?.let {
            AuthenticationState.AUTHENTICATED
        } ?: AuthenticationState.UNAUTHENTICATED
    }

}