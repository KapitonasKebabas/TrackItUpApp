package com.example.trackitupapp.apiServices

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceInstance
{
    companion object {
        private const val BASE_URL = "http://172.20.10.3:8000/"
    }
    object Auth {
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL + "auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiServices: ApiServices = retrofit.create(ApiServices::class.java)
    }

    object Medicine {
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL + "medicine/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiServices: ApiServices = retrofit.create(ApiServices::class.java)
    }
}