package com.udacity.location.reminder.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class AuthenticationViewModel : ViewModel() {

    val authenticationState = FirebaseUserLiveData().map { user ->
        user?.let {
            AuthenticationState.AUTHENTICATED
        } ?: AuthenticationState.UNAUTHENTICATED
    }

}