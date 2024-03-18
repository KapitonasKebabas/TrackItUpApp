package com.example.trackitupapp.dataHolder

import com.example.trackitupapp.apiServices.responses.AprovedMedecineResponse

object AprovedMedicine {
    private var myList = mutableListOf<AprovedMedecineResponse>()

    fun addToList(list: List<AprovedMedecineResponse>) {
        myList = list.toMutableList()
    }

    fun getList(): List<AprovedMedecineResponse> {
        return myList.toList()
    }

    fun getObjectByPk(pk: Int): AprovedMedecineResponse? {
        return myList.find { it.pk == pk }
    }
}