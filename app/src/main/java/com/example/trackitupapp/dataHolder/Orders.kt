package com.example.trackitupapp.dataHolder

import com.example.trackitupapp.apiServices.responses.OrderResponse

object Orders {
    private var myList = mutableListOf<OrderResponse>()

    fun addToList(list: List<OrderResponse>) {
        myList = list.toMutableList()
    }

    fun addItemToList(medicine: OrderResponse) {
        myList.add(medicine)
    }

    fun deleteItemFromList(pk: Int) {
        myList.removeIf { it.pk == pk }
    }

    fun getList(): List<OrderResponse> {
        return myList.toList()
    }

    fun getObjectByPk(pk: Int): OrderResponse? {
        return myList.find { it.pk == pk }
    }
}