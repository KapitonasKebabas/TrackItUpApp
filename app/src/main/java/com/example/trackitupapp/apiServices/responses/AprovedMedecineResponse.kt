package com.example.trackitupapp.apiServices.responses

data class AprovedMedecineResponse(
    val pk: Int,
    val name: String,
    val description: String?,
    val isPrescription: Boolean,
    val photo: String,
    val isApproved: Boolean
)