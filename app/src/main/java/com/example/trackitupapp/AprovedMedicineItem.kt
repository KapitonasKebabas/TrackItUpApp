package com.example.trackitupapp

data class AprovedMedicineItem(val pk: Int, val name: String) {
    override fun toString(): String {
        return name
    }
}
