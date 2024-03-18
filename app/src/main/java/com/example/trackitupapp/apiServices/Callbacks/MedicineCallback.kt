package com.example.trackitupapp.apiServices.Callbacks

import com.example.trackitupapp.apiServices.responses.MedicineResponse

interface MedicineCallback {
    fun onSuccess(message: MedicineResponse)
    fun onFailure(message: String)
}
