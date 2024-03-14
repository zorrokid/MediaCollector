package com.zorrokid.mediacollector.model

sealed class Response<out T> {
    data object Initial: Response<Nothing>()
    data object Loading : Response<Nothing>()
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val message: String) : Response<Nothing>()
}