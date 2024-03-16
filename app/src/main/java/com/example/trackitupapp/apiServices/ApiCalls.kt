package com.example.trackitupapp.apiServices

import android.content.Context
import com.example.trackitupapp.apiServices.Callbacks.LoginCallback
import com.example.trackitupapp.apiServices.Callbacks.RegisterCallback
import com.example.trackitupapp.apiServices.Callbacks.UserMedicineCallback
import com.example.trackitupapp.apiServices.responses.LoginResponse
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.apiServices.responses.RegisterResponse
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

        call.enqueue(object : Callback<List<MedicineResponse>>
        {
            override fun onResponse(call: Call<List<MedicineResponse>>, response: Response<List<MedicineResponse>>)
            {
                if(response.isSuccessful) {
                    param.onSuccess(response.body() ?: emptyList())
                }
                else{
                    param.onFailure("Error")
                }

            }
            override fun onFailure(call: Call<List<MedicineResponse>>, t: Throwable)
            {
                param.onFailure("${t.message}")
            }
        })
    }
}