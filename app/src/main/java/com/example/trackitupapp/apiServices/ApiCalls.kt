package com.example.trackitupapp.apiServices

import AESCrypt
import android.content.Context
import com.example.trackitupapp.activities.LoginActivity
import com.example.trackitupapp.apiServices.Callbacks.AprovedMedicineCallback
import com.example.trackitupapp.apiServices.Callbacks.LoginCallback
import com.example.trackitupapp.apiServices.Callbacks.MedicineCallback
import com.example.trackitupapp.apiServices.Callbacks.RegisterCallback
import com.example.trackitupapp.apiServices.Callbacks.SimpleCallback
import com.example.trackitupapp.apiServices.Callbacks.UserMedicineCallback
import com.example.trackitupapp.apiServices.calls.MedicineCall
import com.example.trackitupapp.apiServices.responses.AprovedMedicinesResponse
import com.example.trackitupapp.apiServices.responses.LoginResponse
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.apiServices.responses.RefreshTokenResponse
import com.example.trackitupapp.apiServices.responses.RegisterResponse
import com.example.trackitupapp.apiServices.responses.SimpleResponse
import com.example.trackitupapp.apiServices.responses.UserMedicineResponse
import com.example.trackitupapp.enums.ProfilePreferences
import com.example.trackitupapp.managers.TokenManager
import com.example.trackitupapp.managers.UserManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCalls {
    var tokenManager    = TokenManager()
    var userManager     = UserManager()

    private fun waitForToken(context: Context)
    {
        while (TokenManager().isTokenUpdating(context)) {
            //TODO(CHECK IF WORKS)
            Thread.sleep(100)
        }
    }

    fun callLogIn(
        applicationContext: Context,
        username: String,
        password: String,
        param: LoginCallback
    )
    {
        val call = ApiServiceInstance.Auth.apiServices.login(username, password)
        call.enqueue(object : Callback<LoginResponse>
        {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>)
            {
                if (response.isSuccessful)
                {
                    val loginResponse = response.body()
                    val token = loginResponse?.access
                    val refreshToken = loginResponse?.refresh

                    if(!token.isNullOrEmpty() && !refreshToken.isNullOrEmpty())
                    {
                        tokenManager.insertTokenResponse(applicationContext, token.toString(), refreshToken.toString())
                        param.onSuccess()
                    }
                    else
                    {
                        param.onFailure("Ivyko klaida")
                    }
                }
                else
                {
                    param.onFailure("Wrong credentials")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable)
            {
                param.onFailure("Connection Error: ${t.message}")
            }
        })
    }

    fun callRegister(username: String, firstName: String, lastName: String, email: String, password: String, param: RegisterCallback)
    {
        val call = ApiServiceInstance.Auth.apiServices.register(username, firstName, lastName, email, password)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.code() == 200) {
                    param.onSuccess(response.message().toString())
                } else {
                    param.onSuccessFail(response)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                param.onFailure(t.toString())
            }
        })
    }

    fun callUserMedicine( applicationContext: Context, param: UserMedicineCallback)
    {
        val call = ApiServiceInstance.Medicine.apiServices.userMedicine(tokenManager.getToken(applicationContext).toString())

        call.enqueue(object : Callback<UserMedicineResponse>
        {
            override fun onResponse(call: Call<UserMedicineResponse>, response: Response<UserMedicineResponse>)
            {
                if(response.isSuccessful) {
                    param.onSuccess(response.body()?.results ?: emptyList())
                }
                else{
                    param.onFailure("Error")
                }

            }
            override fun onFailure(call: Call<UserMedicineResponse>, t: Throwable)
            {
                param.onFailure("${t.message}")
            }
        })
    }

    fun callAprovedMedicine( applicationContext: Context, param: AprovedMedicineCallback)
    {
        val call = ApiServiceInstance.Medicine.apiServices.userAprovedMedicine(tokenManager.getToken(applicationContext).toString())

        call.enqueue(object : Callback<AprovedMedicinesResponse>
        {
            override fun onResponse(call: Call<AprovedMedicinesResponse>, response: Response<AprovedMedicinesResponse>)
            {
                if(response.isSuccessful) {
                    param.onSuccess(response.body()?.results ?: emptyList())
                }
                else{
                    param.onFailure("Error")
                }

            }
            override fun onFailure(call: Call<AprovedMedicinesResponse>, t: Throwable)
            {
                param.onFailure("${t.message}")
            }
        })
    }

    fun callUserMedicineUpdate(applicationContext: Context, userMedicine: MedicineResponse, param: MedicineCallback)
    {
        val call = ApiServiceInstance.Medicine.apiServices.updateUserMedicine(tokenManager.getToken(applicationContext).toString(), userMedicine.pk, userMedicine)

        call.enqueue(object : Callback<MedicineResponse>
        {
            override fun onResponse(call: Call<MedicineResponse>, response: Response<MedicineResponse>)
            {
                if(response.isSuccessful) {
                    param.onSuccess(response.body()!!)
                }
                else{
                    param.onFailure("Error")
                }

            }
            override fun onFailure(call: Call<MedicineResponse>, t: Throwable)
            {
                param.onFailure("${t.message}")
            }
        })
    }

    fun callUserMedicineDelete(applicationContext: Context, userMedicine: MedicineResponse, param: SimpleCallback)
    {
        val call = ApiServiceInstance.Medicine.apiServices.deleteUserMedicine(tokenManager.getToken(applicationContext).toString(), userMedicine.pk)

        call.enqueue(object : Callback<SimpleResponse>
        {
            override fun onResponse(call: Call<SimpleResponse>, response: Response<SimpleResponse>)
            {
                if(response.isSuccessful) {
                    param.onSuccess("Deleted")
                }
                else{
                    param.onFailure("Error")
                }

            }
            override fun onFailure(call: Call<SimpleResponse>, t: Throwable)
            {
                param.onFailure("${t.message}")
            }
        })
    }

    fun callCheckToken(applicationContext: Context, param: SimpleCallback)
    {
        var username = userManager.getUserDataByFieldString(applicationContext, ProfilePreferences.Username.toString())

        if(username != "") {
            val call = ApiServiceInstance.Auth.apiServices.checkauth(
                tokenManager.getToken(applicationContext).toString(),
                AESCrypt.decrypt(username)
            )
            call.enqueue(object : Callback<SimpleResponse> {
                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    if (response.isSuccessful) {
                        param.onSuccess("") // Notify the callback with the result
                    } else {
                        param.onFailure("Error")
                    }
                }

                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    param.onFailure("${t.message}")
                }
            })
        }
    }

    fun callAddUserMedicine(applicationContext: Context, medicine: MedicineCall, param: MedicineCallback)
    {
        val call = ApiServiceInstance.Medicine.apiServices.createUserMedicine(tokenManager.getToken(applicationContext).toString(), medicine)

        call.enqueue(object : Callback<MedicineResponse>
        {
            override fun onResponse(call: Call<MedicineResponse>, response: Response<MedicineResponse>)
            {
                if(response.isSuccessful) {
                    param.onSuccess(response.body()!!)
                }
                else{
                    param.onFailure("Error")
                }

            }
            override fun onFailure(call: Call<MedicineResponse>, t: Throwable)
            {
                param.onFailure("${t.message}")
            }
        })
    }

    fun callRefreshToken(applicationContext: Context, param: LoginCallback) {
        waitForToken(applicationContext)

        val tokenManager = TokenManager()
        val refreshToken = tokenManager.getRefToken(applicationContext)

        val call = ApiServiceInstance.Auth.apiServices.refreshToken(refreshToken.toString())

        call.enqueue(object : Callback<RefreshTokenResponse>
        {
            override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>)
            {
                if(response.isSuccessful){
                    tokenManager.saveToken(applicationContext, response.body()!!.access)
                    param.onSuccess() // Notify the callback with the result
                }
                else{
                    param.onFailure("")
                }
            }

            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable)
            {
                param.onFailure("${t.message}")
            }
        })
    }

}