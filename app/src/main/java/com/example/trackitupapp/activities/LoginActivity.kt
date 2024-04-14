package com.example.trackitupapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.AprovedMedicineCallback
import com.example.trackitupapp.apiServices.Callbacks.LoginCallback
import com.example.trackitupapp.apiServices.Callbacks.SimpleCallback
import com.example.trackitupapp.apiServices.responses.AprovedMedecineResponse
import com.example.trackitupapp.dataHolder.AprovedMedicine
import com.example.trackitupapp.enums.ProfilePreferences
import com.example.trackitupapp.managers.TokenManager
import com.example.trackitupapp.managers.UserManager
import java.util.Timer
import java.util.TimerTask

class LoginActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls


    private fun setButtons()
    {
        val btnLogin = findViewById<Button>(R.id.btn_login)
        btnLogin.setOnClickListener {
            logIn()
        }

        val btnReg = findViewById<Button>(R.id.btn_regActivity)

        btnReg.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            applicationContext.startActivity(intent)
        }
    }

    fun authorized()
    {
        val handler = Handler()
        val timer = Timer()
        val doAsynchronousTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post(Runnable {
                    try {
                        TokenManager().updateAuthToken(this@LoginActivity)
                        Log.i("TokenRefresh", "Refresh")
                    } catch (e: Exception) {
                    }
                })
            }
        }
        timer.schedule(doAsynchronousTask, 0, 660000)

        //TODO FINISH MainActivity

        val intent = Intent(this, UserMedicineActivity::class.java)
        this.startActivity(intent)

        finish()
    }

    fun authDenied(message: String) {
        findViewById<TextView>(R.id.tv_token).text = message
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        calls           = ApiCalls()

        if(UserManager().getUserDataByFieldString(applicationContext, ProfilePreferences.Password.toString()).isNotBlank() &&
            UserManager().getUserDataByFieldString(applicationContext, ProfilePreferences.Username.toString()).isNotBlank()){
            logIn()
        }

        setButtons()

        /*val textView = findViewById<TextView>(R.id.question)
        textView.movementMethod = LinkMovementMethod.getInstance()

        val spannableString = SpannableString(getString(R.string.login_psw_forget))

        val respasswClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Handle click on "Reset password"
                GoToActivities.ResetPasswordActivity(this@LoginActivity)
            }
        }

        // Find the start and end indices of "Reset password" in the text
        val respasswStartIndex = spannableString.indexOf("Reset password")
        val respasswEndIndex = respasswStartIndex + "Reset password".length

        // Set clickable spans for "Reset password"
        spannableString.setSpan(respasswClickableSpan, respasswStartIndex, respasswEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannableString*/
    }

    private fun logIn() {
        var username    = findViewById<EditText>(R.id.username).text.toString()
        var password    = findViewById<EditText>(R.id.password).text.toString()
        val progressBar = findViewById<ProgressBar>(R.id.pb_loading)

        progressBar.visibility = View.VISIBLE

        if(username.isBlank() || password.isBlank())
        {
            username = UserManager().getUserDataByFieldString(applicationContext, ProfilePreferences.Username.toString())
            password = UserManager().getUserDataByFieldString(applicationContext, ProfilePreferences.Password.toString())
            if(username.isBlank() || password.isBlank()) {
                authDenied("Abu laukai turi buti uzpildyti")
                return
            }
        }

        calls.callLogIn(
            applicationContext,
            username,
            password,
            object : LoginCallback {
                override fun onSuccess() {
                    UserManager().saveDataByFieldString(applicationContext, ProfilePreferences.Username.toString(), username)
                    UserManager().saveDataByFieldString(applicationContext, ProfilePreferences.Password.toString(), password)
                    authorized()
                    progressBar.visibility = View.GONE
                }

                override fun onFailure(message: String) {
                    authDenied(message)
                    progressBar.visibility = View.GONE
                }
            }
        )
    }
}
