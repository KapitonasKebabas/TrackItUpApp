package com.example.trackitupapp.apiServices.calls

data class OrderCall(
    val user_seller_id: Int,
    var user_medicine_pk: Int,
    var qty: Int,
    var status: Int
)
