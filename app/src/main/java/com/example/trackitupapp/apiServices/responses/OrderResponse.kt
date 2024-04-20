package com.example.trackitupapp.apiServices.responses

data class OrderResponse(
    val pk: Int,
    val aproved_medecine: Int,
    val medecine: Int,
    val medecine_name: String,
    val user_seller_pk: Int,
    val user_seller_username: String,
    val user_buyer_pk: Int,
    val user_buyer_username: String,
    val user_medicine_pk: Int,
    val qty: Int,
    val status: Int,
    val status_name: String,
    val status_helptext: String
)
