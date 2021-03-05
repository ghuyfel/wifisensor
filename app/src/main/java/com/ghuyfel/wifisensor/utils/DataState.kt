package com.ghuyfel.wifisensor.utils

import java.lang.Exception

sealed class DataState<out R> {
    data class Success<out T>(val data: T): DataState<T>()
    data class Error(val e: Exception): DataState<Nothing>()
}
