package com.example.trackitupapp.apiServices


import com.example.trackitupapp.apiServices.responses.LoginResponse
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.apiServices.responses.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiServices {

    @POST("login/")
    @FormUrlEncoded
    fun login(
        @Field("username") usernameLog: String,
        @Field("password") passwordLog: String
    ): Call<LoginResponse>

    @POST("register/")
    @FormUrlEncoded
    fun register(
        @Field("username")      usernameReg:    String,
        @Field("first_name")    firstNameReg:   String,
        @Field("last_name")     lastNameReg:    String,
        @Field("email")         emailReg:       String,
        @Field("password")      passwordReg:    String
    ): Call<RegisterResponse>

    @GET("list/")
    fun userMedicine(
        @Header("Authorization") token: String
    ): Call<List<MedicineResponse>>
}
