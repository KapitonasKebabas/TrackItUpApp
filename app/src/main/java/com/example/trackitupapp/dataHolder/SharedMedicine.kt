package com.example.trackitupapp.dataHolder

import com.example.trackitupapp.apiServices.responses.SharedMedicineResponse

object SharedMedicine {
    private var myList = mutableListOf<SharedMedicineResponse>()

    fun addToList(list: List<SharedMedicineResponse>) {
        myList = list.toMutableList()
    }

    fun addItemToList(medicine: SharedMedicineResponse) {
        myList.add(medicine)
    }

    fun deleteItemFromList(pk: Int) {
        myList.removeIf { it.pk == pk }
    }

    fun getList(): List<SharedMedicineResponse> {
        return myList.toList()
    }

    fun getObjectByPk(pk: Int): SharedMedicineResponse? {
        return myList.find { it.pk == pk }
    }
}