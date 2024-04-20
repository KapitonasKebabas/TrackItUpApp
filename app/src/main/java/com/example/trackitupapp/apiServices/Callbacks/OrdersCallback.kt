package com.example.trackitupapp.apiServices.Callbacks

import com.example.trackitupapp.apiServices.responses.OrderResponse

interface OrdersCallback {
    fun onSuccess(ordersResponse: List<OrderResponse>)
    fun onFailure(message: String)
}