package com.example.trackitupapp.dataHolder

import com.example.trackitupapp.apiServices.responses.MedicineResponse

object UserMedicine {
    private var myList = mutableListOf<MedicineResponse>()

    fun addToList(list: List<MedicineResponse>) {
        myList = list.toMutableList()
    }

    fun getList(): List<MedicineResponse> {
        return myList.toList()
    }
}