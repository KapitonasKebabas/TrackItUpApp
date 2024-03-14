package com.example.trackitupapp.managers

import android.content.Context
import android.content.SharedPreferences

class TokenManager {
    companion object {
        private const val PREF_NAME = "app_preferences"
        private const val TOKEN_KEY = "token"
    }

    fun saveToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences    = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor        = sharedPreferences.edit()

        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun deleteToken(context: Context) {
        val sharedPreferences   = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor              = sharedPreferences.edit()

        editor.remove(TOKEN_KEY)
        editor.apply()
    }
}
