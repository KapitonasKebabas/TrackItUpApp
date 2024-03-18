package com.example.trackitupapp.apiServices

import AESCrypt
import android.content.Context
import com.example.trackitupapp.apiServices.Callbacks.AprovedMedicineCallback
import com.example.trackitupapp.apiServices.Callbacks.LoginCallback
import com.example.trackitupapp.apiServices.Callbacks.MedicineCallback
import com.example.trackitupapp.apiServices.Callbacks.RegisterCallback
import com.example.trackitupapp.apiServices.Callbacks.SimpleCallback
import com.example.trackitupapp.apiServices.Callbacks.UserMedicineCallback
import com.example.trackitupapp.apiServices.responses.AprovedMedicinesResponse
import com.example.trackitupapp.apiServices.responses.LoginResponse
import com.example.trackitupapp.apiServices.responses.MedicineResponse
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

    fun callLogIn(
        applicationContext: Context,
        username: String,
        password: String,
        param: LoginCallback
    )
    {
        val call = ApiServiceInstance.Auth.apiServices.login(username, AESCrypt.encrypt(password))
        call.enqueue(object : Callback<LoginResponse>
        {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>)
            {
                if (response.isSuccessful)
                {
                    val loginResponse = response.body()
                    val tokenResponse = loginResponse?.token

                    if(!tokenResponse.isNullOrEmpty())
                    {
                        tokenManager.saveToken(applicationContext, tokenResponse.toString())
                        userManager.saveDataByFieldString(applicationContext, ProfilePreferences.Username.toString(), AESCrypt.encrypt(username))
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
        val call = ApiServiceInstance.Auth.apiServices.register(username, firstName, lastName, email, AESCrypt.encrypt(password))

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
        val call = ApiServiceInstance.Medicine.apiServices.userMedicine("Token " + tokenManager.getToken(applicationContext).toString())

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
        val call = ApiServiceInstance.Medicine.apiServices.userAprovedMedicine("Token " + tokenManager.getToken(applicationContext).toString())

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
        val call = ApiServiceInstance.Medicine.apiServices.updateUserMedicine("Token " + tokenManager.getToken(applicationContext).toString(), userMedicine.pk, userMedicine)

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
        val call = ApiServiceInstance.Medicine.apiServices.deleteUserMedicine("Token " + tokenManager.getToken(applicationContext).toString(), userMedicine.pk)

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
        val username = userManager.getUserDataByFieldString(applicationContext, ProfilePreferences.Username.toString())

        if(username != "") {
            val call = ApiServiceInstance.Auth.apiServices.checkauth("Token " + tokenManager.getToken(applicationContext).toString(), AESCrypt.decrypt(username))
            call.enqueue(object : Callback<SimpleResponse> {
                override fun onResponse(call: Call<SimpleResponse>, response: Response<SimpleResponse>) {
                    if(response.isSuccessful) {
                        param.onSuccess("") // Notify the callback with the result
                    }
                    else
                    {
                        param.onFailure("Error")
                    }
                }

                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    param.onFailure("${t.message}")
                }
            })
        }
    }

    fun callAddUserMedicine(applicationContext: Context, medicine: MedicineResponse, param: MedicineCallback)
    {
        val call = ApiServiceInstance.Medicine.apiServices.createUserMedicine("Token " + tokenManager.getToken(applicationContext).toString(), medicine)

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
}