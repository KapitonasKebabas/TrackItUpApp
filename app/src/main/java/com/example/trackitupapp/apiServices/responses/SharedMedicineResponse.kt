package com.example.trackitupapp.apiServices.responses

data class SharedMedicineResponse(
    val pk: Int,
    val medecine: Int,
    val medecine_name: String,
    val medecine_description: String,
    val shared_qty: Int,
    val user_pk: Int,
    val user_name: String
)