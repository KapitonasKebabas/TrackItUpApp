package com.example.trackitupapp.apiServices.Callbacks

import com.example.trackitupapp.apiServices.responses.RegisterResponse
import retrofit2.Response

interface RegisterCallback {
    fun onSuccess(message: String)
    fun onSuccessFail(message: Response<RegisterResponse>)
    fun onFailure(message: String)
}