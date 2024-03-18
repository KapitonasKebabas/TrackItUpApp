package com.example.trackitupapp.apiServices.calls

data class MedicineCall(
    val medecine: Int,
    var qty: Int,
    var exp_date: String,
    var is_shared: Boolean,
    val shared_qty: Int
)
