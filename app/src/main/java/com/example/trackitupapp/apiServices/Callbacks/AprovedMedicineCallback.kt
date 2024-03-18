package com.example.trackitupapp.apiServices.Callbacks

import com.example.trackitupapp.apiServices.responses.AprovedMedecineResponse

interface AprovedMedicineCallback {
    fun onSuccess(medicineResponse: List<AprovedMedecineResponse>)
    fun onFailure(message: String)
}