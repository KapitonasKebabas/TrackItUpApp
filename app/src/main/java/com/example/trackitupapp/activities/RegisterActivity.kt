package com.example.trackitupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.RegisterCallback
import com.example.trackitupapp.apiServices.responses.RegisterResponse
import com.example.trackitupapp.enums.ProfilePreferences
import org.json.JSONObject
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    private fun generateError(response: Response<RegisterResponse>): String {
        val errorResponse = response.errorBody()?.string()
        val jObjError = JSONObject(errorResponse ?: "")
        val errorMessageBuilder = StringBuilder()

        // Access the "response" object
        if (jObjError.has("response")) {
            val responseObj = jObjError.getJSONObject("response")

            if (responseObj.has(ProfilePreferences.Username.toString())) {
                val usernameErrors =
                    responseObj.getJSONArray(ProfilePreferences.Username.toString())

                for (i in 0 until usernameErrors.length()) {
                    //TODO handle error
                    errorMessageBuilder.append(usernameErrors.optString(i))
                    errorMessageBuilder.append("\n")
                }
            }

            if (responseObj.has(ProfilePreferences.Email.toString())) {
                val emailErrors =
                    responseObj.getJSONArray(ProfilePreferences.Email.toString())

                for (i in 0 until emailErrors.length()) {
                    //TODO handle error
                    errorMessageBuilder.append(emailErrors.optString(i))
                    errorMessageBuilder.append("\n")
                }
            }
        }

        return errorMessageBuilder.toString()
    }

    private fun callRegister(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        val calls = ApiCalls()

        calls.callRegister(
            username,
            firstName,
            lastName,
            email,
            password,
            object : RegisterCallback {
                override fun onSuccess(message: String) {
                    //TODO Handle message
                    finish()
                }

                override fun onSuccessFail(message: Response<RegisterResponse>) {
                    generateError(message)
                }

                override fun onFailure(message: String) {
                    //TODO Handle message
                }
            }
        )

    }
}