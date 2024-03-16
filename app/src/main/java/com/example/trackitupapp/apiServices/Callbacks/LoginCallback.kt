package com.example.trackitupapp.apiServices.Callbacks

interface LoginCallback {
    fun onSuccess()
    fun onFailure(message: String)
}
