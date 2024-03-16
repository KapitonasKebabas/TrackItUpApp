package com.example.trackitupapp.apiServices

import android.content.Context
import com.example.trackitupapp.apiServices.Callbacks.LoginCallback
import com.example.trackitupapp.apiServices.responses.LoginResponse
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

}