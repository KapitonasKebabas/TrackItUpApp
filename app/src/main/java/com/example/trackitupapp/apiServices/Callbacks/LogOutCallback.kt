package com.example.trackitupapp.apiServices.Callbacks

interface LogOutCallback {
    fun onSuccess()
    fun onFailure(message: String)
}