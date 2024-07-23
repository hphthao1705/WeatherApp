package com.example.weatherapp.data.base

import retrofit2.Response

object ApiHelper {
    private val otherErrorMessage = "Unknown error!"
    suspend fun <T> launch(
        apiCall: suspend () -> Response<T>,
        onSuccess: suspend (T?) -> Unit,
        catchOnErrorResponse: (suspend (T) -> Unit)? = null,
        catchOnHttpError: (suspend (ErrorData) -> Unit)? = null,
    ) {
        try {
            val result = apiCall()
            if (result.isSuccessful) {
                onSuccess(result.body())
            } else {
                catchOnHttpError?.invoke(
                    ErrorData(result.code(), result.message())
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            catchOnHttpError?.invoke(ErrorData(400, ""))
        }
    }
}