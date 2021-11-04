package com.avisio.dashboard.common.controller

sealed class State<T> {

    data class Loading<T>(val data: T) : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Failure<T>(val message: String) : State<T>()

    companion object {
        fun <T> loading(data: T) = Loading(data)
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failure<T>(message)
    }

}