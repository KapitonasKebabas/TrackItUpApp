package com.example.trackitupapp.apiServices.Callbacks

import com.example.trackitupapp.apiServices.responses.MedicineResponse
interface UserMedicineCallback {
    fun onSuccess(medicineResponse: List<MedicineResponse>)
    fun onFailure(message: String)
}