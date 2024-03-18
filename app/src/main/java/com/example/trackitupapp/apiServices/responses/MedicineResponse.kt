package com.example.trackitupapp.apiServices.responses

data class MedicineResponse(
    val pk: Int,
    val medecine: Int,
    val medecine_name: String,
    val medecine_description: String,
    val medecine_is_prescription: Boolean,
    val qty: Int,
    val exp_date: String,
    val is_shared: Boolean,
    val shared_qty: Int
)