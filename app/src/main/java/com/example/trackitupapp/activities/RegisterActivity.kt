package com.example.trackitupapp.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.RegisterCallback
import com.example.trackitupapp.apiServices.responses.RegisterResponse
import com.example.trackitupapp.enums.ProfilePreferences
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var calls: ApiCalls
    private fun handleError(response: Response<RegisterResponse>) {
        val errorResponse = response.errorBody()?.string()
        val jObjError = errorResponse?.let { JSONObject(it) }


        // Access the "response" object
        if(jObjError?.has("response") == true) {
            val responseObj = jObjError.getJSONObject("response")

            if (responseObj.has(ProfilePreferences.Username.toString())) {
                val errorMessageBuilder = StringBuilder()
                val usernameErrors =
                    responseObj.getJSONArray(ProfilePreferences.Username.toString())

                for (i in 0 until usernameErrors.length()) {
                    errorMessageBuilder.append(usernameErrors.optString(i))
                    errorMessageBuilder.append("\n")
                }

                findViewById<TextView>(R.id.tv_userError).text = errorMessageBuilder.toString()
            }

            if (responseObj.has(ProfilePreferences.Email.toString())) {
                val errorMessageBuilder = StringBuilder()
                val emailErrors =
                    responseObj.getJSONArray(ProfilePreferences.Email.toString())

                for (i in 0 until emailErrors.length()) {
                    errorMessageBuilder.append(emailErrors.optString(i))
                    errorMessageBuilder.append("\n")
                }

                findViewById<TextView>(R.id.tv_emailError).text = errorMessageBuilder.toString()
            }

            if (responseObj.has(ProfilePreferences.Password.toString())) {
                val errorMessageBuilder = StringBuilder()
                val pswErrors =
                    responseObj.getJSONArray(ProfilePreferences.Password.toString())

                for (i in 0 until pswErrors.length()) {
                    errorMessageBuilder.append(pswErrors.optString(i))
                    errorMessageBuilder.append("\n")
                }

                findViewById<TextView>(R.id.tv_pswError).text = errorMessageBuilder.toString()
            }
        }
    }

    private fun isOldEnough(birthDateString: String, requiredAge: Int): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val birthDate: Date? = try {
            dateFormat.parse(birthDateString)
        } catch (e: Exception) {
            null
        }

        birthDate?.let {
            val calendar = Calendar.getInstance()
            val today = Calendar.getInstance()

            calendar.time = birthDate
            val age = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)

            // Adjust if the birthday hasn't occurred yet this year
            if (today.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)) {
                return age - 1 >= requiredAge
            }
            return age >= requiredAge
        }

        return false // Return false if date parsing fails
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
            val gimimas = findViewById<EditText>(R.id.et_gimimo_registerAct).text.toString()

            if (!isOldEnough(gimimas, 16)) {
                //Toast.makeText(this, "Negali būti mažiau negu 16 metų", Toast.LENGTH_SHORT).show()
                findViewById<TextView>(R.id.tv_pswError).text = "Negali būti mažiau negu 16 metų"
            }
            else
            {
                calls.callRegister(
                    username,
                    firstName,
                    lastName,
                    email,
                    password,
                    object : RegisterCallback
                    {
                        override fun onSuccess(message: String) {
                            Toast.makeText(applicationContext, "Sekmingai prisiregistravote", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        override fun onSuccessFail(message: Response<RegisterResponse>) {
                            handleError(message)
                        }

                        override fun onFailure(message: String) {
                            //callMessage(message)
                        }
                    }
                )
            }


        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
            findViewById<EditText>(R.id.et_gimimo_registerAct).setText(date)
        }, year, month, day)

        datePickerDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        calls = ApiCalls()
        findViewById<EditText>(R.id.et_gimimo_registerAct).setOnClickListener {
            showDatePickerDialog()
        }
        btnReg()
    }
}