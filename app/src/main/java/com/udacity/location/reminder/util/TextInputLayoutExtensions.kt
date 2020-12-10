package com.udacity.location.reminder.util

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.validateErrorInputLayout() {
    if (isErrorEnabled) {
        error = null
    }
}

fun TextInputLayout.showErrorMessageInputLayout(message: String) {
    isErrorEnabled = true
    error = message
}