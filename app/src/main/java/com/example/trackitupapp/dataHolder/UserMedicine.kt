package com.example.trackitupapp.dataHolder

import com.example.trackitupapp.apiServices.responses.AprovedMedecineResponse
import com.example.trackitupapp.apiServices.responses.MedicineResponse

object UserMedicine {
    private var myList = mutableListOf<MedicineResponse>()

    fun addToList(list: List<MedicineResponse>) {
        myList = list.toMutableList()
    }

    fun addItemToList(medicine: MedicineResponse) {
        myList.add(medicine)
    }

    fun getList(): List<MedicineResponse> {
        return myList.toList()
    }

    fun getObjectByPk(pk: Int): MedicineResponse? {
        return UserMedicine.myList.find { it.pk == pk }
    }
}