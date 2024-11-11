package com.example.trackitupapp.helpers

import android.content.Context
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.OrderCallback
import com.example.trackitupapp.apiServices.calls.OrderCall
import com.example.trackitupapp.apiServices.responses.SharedMedicineResponse

class OrderManager(private val apiCalls: ApiCalls) {

    fun createOrder(
        context: Context,
        sharedMedicine: SharedMedicineResponse, // Use SharedMedicineResponse instead of SharedMedicine
        quantity: Int,
        callback: OrderCallback
    ) {
        val order = OrderCall(sharedMedicine.user_pk, sharedMedicine.pk, quantity, 3)
        apiCalls.callAddOrder(context, order, callback)
    }
}
