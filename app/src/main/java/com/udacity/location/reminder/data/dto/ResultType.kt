package com.udacity.location.reminder.data.dto

sealed class ResultType<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResultType<T>()
    data class Error(val message: String?, val statusCode: Int? = null) :
        ResultType<Nothing>()
}