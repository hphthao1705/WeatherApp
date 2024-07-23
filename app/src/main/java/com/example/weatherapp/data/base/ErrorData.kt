package com.example.weatherapp.data.base

class ErrorData(code: Int, message: String) {
    val code: Int
    val message: String

    init {
        this.code = code
        this.message = message
    }

}