package com.example.trackitupapp.apiServices.Callbacks

import com.example.trackitupapp.apiServices.responses.StatusResponse

interface StatusesCallback {
    fun onSuccess(statuses: List<StatusResponse>)
    fun onFailure(message: String)
}