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

    private lateinit var calls: ApiCalls
    private fun generateError(response: Response<RegisterResponse>) : String {
        val errorResponse = response.errorBody()?.string()
        val jObjError = errorResponse?.let { JSONObject(it) }
        val errorMessageBuilder = StringBuilder()

        // Access the "response" object
        if(jObjError?.has("response") == true) {
            val responseObj = jObjError.getJSONObject("response")

            if (responseObj.has(ProfilePreferences.Username.toString())) {
                val usernameErrors =
                    responseObj.getJSONArray(ProfilePreferences.Username.toString())

                for (i in 0 until usernameErrors.length()) {
                    errorMessageBuilder.append(usernameErrors.optString(i))
                    errorMessageBuilder.append("\n")
                }
            }

            if (responseObj.has(ProfilePreferences.Email.toString())) {
                val emailErrors =
                    responseObj.getJSONArray(ProfilePreferences.Email.toString())

                for (i in 0 until emailErrors.length()) {
                    errorMessageBuilder.append(emailErrors.optString(i))
                    errorMessageBuilder.append("\n")
                }
            }

            if (responseObj.has(ProfilePreferences.Password.toString())) {
                val pswErrors =
                    responseObj.getJSONArray(ProfilePreferences.Password.toString())

                for (i in 0 until pswErrors.length()) {
                    errorMessageBuilder.append(pswErrors.optString(i))
                    errorMessageBuilder.append("\n")
                }
            }
        }

        return errorMessageBuilder.toString()
    }

    private fun btnReg() {
        val btnReg = findViewById<Button>(R.id.btn_reg_registerAct)
        btnReg.setOnClickListener()
        {
            val username    = findViewById<TextView>(R.id.et_username_registerAct).text.toString()
            val firstName   = findViewById<TextView>(R.id.et_firstName_registerAct).text.toString()
            val lastName    = findViewById<TextView>(R.id.et_lastName_registerAct).text.toString()
            val email       = findViewById<TextView>(R.id.et_email_registerAct).text.toString()
            val password    = findViewById<TextView>(R.id.et_password_registerAct).text.toString()

            calls.callRegister(
                username,
                firstName,
                lastName,
                email,
                password,
                object : RegisterCallback
                {
                    override fun onSuccess(message: String) {
                        callMessage(message)
                        finish()
                    }

                    override fun onSuccessFail(message: Response<RegisterResponse>) {
                        callMessage(generateError(message))
                    }

                    override fun onFailure(message: String) {
                        callMessage(message)
                    }
                }
            )
        }
    }

    private fun callMessage(message: String?)
    {
        val messageField = findViewById<TextView>(R.id.tv_registerError)
        messageField.text = message
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        calls = ApiCalls()

        btnReg()
    }
}