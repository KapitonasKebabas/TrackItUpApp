package com.example.trackitupapp.apiServices.Callbacks

import com.example.trackitupapp.apiServices.responses.OrderResponse

interface OrdersCallback {
    fun onSuccess(medicineResponse: List<OrderResponse>)
    fun onFailure(message: String)
}