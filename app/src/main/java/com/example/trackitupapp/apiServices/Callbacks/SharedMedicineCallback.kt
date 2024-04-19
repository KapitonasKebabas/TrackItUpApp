package com.example.trackitupapp.apiServices.Callbacks

import com.example.trackitupapp.apiServices.responses.SharedMedicineResponse

interface SharedMedicineCallback {
    fun onSuccess(medicineResponse: List<SharedMedicineResponse>)
    fun onFailure(message: String)
}