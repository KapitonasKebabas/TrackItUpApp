package com.example.trackitupapp.managers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.trackitupapp.activities.LoginActivity
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.LoginCallback
import com.example.trackitupapp.enums.ProfilePreferences
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TokenManager {
    companion object {
        private const val PREF_NAME             = "app_preferences"
        private const val TOKEN_KEY             = "token"
        private const val TOKEN_REF             = "refreshToken"
        private const val TOKEN_LAST_CHECK_DAY  = "tokenLastDay"
        private const val TOKEN_LAST_CHECK_TIME = "tokenLastTime"
        private const val TOKEN_TIME            = 600000
        private const val TOKEN_TYPE            = "Bearer"
        private const val TOKEN_IS_UPDATING     = "tokenIsUpdating"
    }

    fun insertTokenResponse(context: Context, access: String, refresh: String) {
        saveToken(context, access)
        saveRefreshToken(context, refresh)
        if(isTokenUpdating(context)){
            isTokenUpdating(context, true)
        }
    }

    fun saveToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences    = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor        = sharedPreferences.edit()

        editor.putString(TOKEN_KEY, "$TOKEN_TYPE $token")
        editor.apply()
        saveLastCheck(context)
    }

    fun saveRefreshToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences    = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor        = sharedPreferences.edit()

        editor.putString(TOKEN_REF, token)
        editor.apply()
    }

    fun saveLastCheck(context: Context) {
        val sharedPreferences: SharedPreferences    = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor        = sharedPreferences.edit()

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

        editor.putString(TOKEN_LAST_CHECK_DAY, dateFormatter)
        editor.putString(TOKEN_LAST_CHECK_TIME, timeFormatter)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun getRefToken(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        return sharedPreferences.getString(TOKEN_REF, null)
    }

    fun isTokenValid(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val storedDate = sharedPreferences.getString(TOKEN_LAST_CHECK_DAY, null)
        val storedTime = sharedPreferences.getString(TOKEN_LAST_CHECK_TIME, null)

        try {
            if (storedDate != null && storedTime != null) {
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                val storedDateTime = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                ).parse("$storedDate $storedTime")!!.time
                val currentDateTime = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                ).parse("$currentDate $currentTime")!!.time

                val differenceInMillis = currentDateTime - storedDateTime

                return differenceInMillis <= TOKEN_TIME
            }
        }
        catch (e: Exception)
        {
            return false
        }

        return false
    }

    fun isTokenUpdating(context: Context, changeStatus: Boolean = false): Boolean
    {
        val sharedPreferences: SharedPreferences    = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor        = sharedPreferences.edit()
        var status = false
        var currentStatus = sharedPreferences.getBoolean(TOKEN_IS_UPDATING, false)

        if(changeStatus) {
            editor.putBoolean(TOKEN_IS_UPDATING, !currentStatus)
            editor.apply()
            currentStatus = !currentStatus
        }


        return currentStatus
    }

    fun deleteToken(context: Context) {
        val sharedPreferences   = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor              = sharedPreferences.edit()

        editor.remove(TOKEN_KEY)
        editor.remove(TOKEN_REF)
        editor.remove(TOKEN_LAST_CHECK_DAY)
        editor.remove(TOKEN_LAST_CHECK_TIME)
        editor.apply()
    }

    fun updateAuthToken(context: Context)
    {
        if(!isTokenValid(context)){
            val calls = ApiCalls()
            if(!TokenManager().isTokenUpdating(context)) {
                TokenManager().isTokenUpdating(context, true)
            }
            refresh(context, calls)
        }
    }

    private fun refresh(context: Context, calls: ApiCalls){
        calls.callRefreshToken(
            context,
            object: LoginCallback
            {
                override fun onSuccess() {
                    if(TokenManager().isTokenUpdating(context)) {
                        TokenManager().isTokenUpdating(context, true)
                    }
                    return
                }

                override fun onFailure(message: String) {
                    deleteToken(context)
                    oldDataLogin(context, calls)
                }

            }
        )
    }

    private fun oldDataLogin(context: Context, calls: ApiCalls)
    {
        val profile = UserManager()

        val username = profile.getUserDataByFieldString(context, ProfilePreferences.Username.toString())
        val password = profile.getUserDataByFieldString(context, ProfilePreferences.Password.toString())

        calls.callLogIn(
            context,
            username,
            password,
            object: LoginCallback
            {
                override fun onSuccess() {
                    if(TokenManager().isTokenUpdating(context)) {
                        TokenManager().isTokenUpdating(context, true)
                    }
                    return
                }

                override fun onFailure(message: String) {
                    if(TokenManager().isTokenUpdating(context)) {
                        TokenManager().isTokenUpdating(context, true)
                    }
                    profile.deleteUserDataByField(context, ProfilePreferences.Password.toString())
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                    return
                }
            }
        )
    }
}
