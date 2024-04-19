package com.example.trackitupapp.apiServices.Callbacks

import com.example.trackitupapp.apiServices.responses.OrderResponse

interface OrderCallback {
    fun onSuccess(medicine: OrderResponse)
    fun onFailure(message: String)
}