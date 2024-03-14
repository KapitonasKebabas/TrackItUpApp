package com.example.trackitupapp.apiServices


import com.example.trackitupapp.apiServices.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServices {

    @POST("login/")
    @FormUrlEncoded
    fun login(
        @Field("username") usernameLog: String,
        @Field("password") passwordLog: String
    ): Call<LoginResponse>
}
