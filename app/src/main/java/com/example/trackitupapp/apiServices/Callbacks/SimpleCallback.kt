package com.example.trackitupapp.apiServices.Callbacks

import com.example.trackitupapp.apiServices.responses.MedicineResponse

interface SimpleCallback {
    fun onSuccess(message: String)
    fun onFailure(message: String)
}