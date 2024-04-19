package com.example.trackitupapp.dataHolder

import com.example.trackitupapp.apiServices.responses.StatusResponse

object Statuses {
    private var myList = mutableListOf<StatusResponse>()

    fun addToList(list: List<StatusResponse>) {
        myList = list.toMutableList()
    }

    fun addItemToList(medicine: StatusResponse) {
        myList.add(medicine)
    }

    fun deleteItemFromList(pk: Int) {
        myList.removeIf { it.pk == pk }
    }

    fun getList(): List<StatusResponse> {
        return myList.toList()
    }

    fun getObjectByPk(pk: Int): StatusResponse? {
        return myList.find { it.pk == pk }
    }
}