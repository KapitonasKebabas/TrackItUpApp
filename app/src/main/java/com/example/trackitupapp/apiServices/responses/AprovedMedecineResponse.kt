package com.example.trackitupapp.apiServices.responses

data class AprovedMedecineResponse(
    val name: String,
    val description: String?,
    val isPrescription: Boolean,
    val photoUrl: String,
    val isApproved: Boolean
)