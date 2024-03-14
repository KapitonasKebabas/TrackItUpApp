package com.example.trackitupapp.apiServices.Interfaces

interface LoginCallback {
    fun onSuccess()
    fun onFailure(message: String)
}
