package com.example.trackitupapp.managers

import AESCrypt
import android.content.Context
import android.content.SharedPreferences
import com.example.trackitupapp.enums.ProfilePreferences

class UserManager {
    private lateinit var sharedPreferences: SharedPreferences

    public fun saveDataByFieldString(context: Context, fieldname: String, value: String) {
        sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        var editor: SharedPreferences.Editor    = sharedPreferences.edit()
        val encryptedData                       = AESCrypt.encrypt(value)

        editor.putString(fieldname, encryptedData)
        editor.apply()
    }

    public fun saveDataByFieldBoolean(context: Context, fieldname: String, value: Boolean) {
        sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        var editor: SharedPreferences.Editor    = sharedPreferences.edit()

        editor.putBoolean(fieldname, value)
        editor.apply()
    }

    public fun getUserDataByFieldString(context: Context, fieldname: String): String {
        sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        var decryptedData = ""

        if(sharedPreferences.getString(fieldname, null) != null)
        {
            decryptedData = AESCrypt.decrypt(sharedPreferences.getString(fieldname, null).toString())
        }

        return decryptedData
    }

    public fun getUserDataByFieldBoolean(context: Context, fieldname: String): Boolean {
        sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(fieldname, false)
    }

    public fun deleteUserDataByField(context: Context, fieldname: String) {
        sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        var editor: SharedPreferences.Editor    = sharedPreferences.edit()

        editor.remove(fieldname)
        editor.apply()
    }

    public fun deleteProfile(context: Context) {
        sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor    = sharedPreferences.edit()

        editor.remove(ProfilePreferences.UserId.toString())
        editor.remove(ProfilePreferences.Username.toString())
        editor.remove(ProfilePreferences.Password.toString())
        editor.remove(ProfilePreferences.Email.toString())
        editor.remove(ProfilePreferences.FirstName.toString())
        editor.remove(ProfilePreferences.LastName.toString())
        editor.remove(ProfilePreferences.IsStaff.toString())
        editor.apply()
    }
}