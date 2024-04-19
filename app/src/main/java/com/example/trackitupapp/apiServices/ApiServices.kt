package com.example.trackitupapp.apiServices


import com.example.trackitupapp.apiServices.calls.MedicineCall
import com.example.trackitupapp.apiServices.calls.OrderCall
import com.example.trackitupapp.apiServices.responses.AprovedMedecineResponse
import com.example.trackitupapp.apiServices.responses.AprovedMedicinesResponse
import com.example.trackitupapp.apiServices.responses.LoginResponse
import com.example.trackitupapp.apiServices.responses.LogoutResponse
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.apiServices.responses.OrderResponse
import com.example.trackitupapp.apiServices.responses.OrdersResponse
import com.example.trackitupapp.apiServices.responses.RefreshTokenResponse
import com.example.trackitupapp.apiServices.responses.RegisterResponse
import com.example.trackitupapp.apiServices.responses.SharedMedicinesResponse
import com.example.trackitupapp.apiServices.responses.SimpleResponse
import com.example.trackitupapp.apiServices.responses.StatusesResponse
import com.example.trackitupapp.apiServices.responses.UserMedicineResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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
    ): Call<UserMedicineResponse>

    @GET("order/statuses/")
    fun statuses(
        @Header("Authorization") token: String
    ): Call<StatusesResponse>

    @GET("shared/list/")
    fun sharedMedicine(
        @Header("Authorization") token: String
    ): Call<SharedMedicinesResponse>

    @GET("order/list/")
    fun orders(
        @Header("Authorization") token: String
    ): Call<OrdersResponse>

    @PUT("order/update/{id}/")
    fun updateOrder(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body userMedicine: OrderResponse
    ): Call<OrderResponse>

    @GET("aproved/list/")
    fun userAprovedMedicine(
        @Header("Authorization") token: String
    ): Call<AprovedMedicinesResponse>

    @DELETE("delete/{id}/")
    fun deleteUserMedicine(
        @Header("Authorization") token: String,
        @Path("id") id: Int

    ): Call<SimpleResponse>

    @PUT("update/{id}/")
    fun updateUserMedicine(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body userMedicine: MedicineResponse
    ): Call<MedicineResponse>

    @POST("checkauth/")
    @FormUrlEncoded
    fun checkauth(
        @Header("Authorization") token: String,
        @Field("username") username: String
    ): Call<SimpleResponse>

    @POST("add/") // Adjust the endpoint as needed
    fun createUserMedicine(
        @Header("Authorization") token: String,
        @Body userMedicine: MedicineCall // Pass the UserMedicine object in the request body
    ): Call<MedicineResponse>

    @POST("order/add/") // Adjust the endpoint as needed
    fun createOrder(
        @Header("Authorization") token: String,
        @Body order: OrderCall // Pass the UserMedicine object in the request body
    ): Call<OrderResponse>

    @POST("refresh/")
    @FormUrlEncoded
    fun refreshToken(
        @Field("refresh") refresh: String
    ): Call<RefreshTokenResponse>

    @POST("auth/delete/")
    @FormUrlEncoded
    fun logout(
        @Header("Authorization") token: String,
        @Field("refresh") refresh: String
    ): Call<LogoutResponse>
}
