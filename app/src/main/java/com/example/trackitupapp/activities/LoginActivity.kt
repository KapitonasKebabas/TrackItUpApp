package com.example.trackitupapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.LoginCallback
import com.example.trackitupapp.managers.TokenManager
import com.example.trackitupapp.managers.UserManager

class LoginActivity : AppCompatActivity() {
    private lateinit var tokenManager: TokenManager
    private lateinit var userManager: UserManager
    private lateinit var calls: ApiCalls
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tokenManager    = TokenManager()
        userManager     = UserManager()
        calls           = ApiCalls()

        btnLogin()
    }

    private fun gotoNextActivity()
    {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun btnLogin()
    {
        val btnLogin = findViewById<Button>(R.id.btn_login)
        btnLogin.setOnClickListener {
            val username    = findViewById<EditText>(R.id.username).text.toString()
            val password    = findViewById<EditText>(R.id.password).text.toString()
            val progressBar = findViewById<ProgressBar>(R.id.pb_loading)

            progressBar.visibility = View.VISIBLE
            calls.callLogIn(
                applicationContext,
                username,
                password,
                object : LoginCallback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                        gotoNextActivity()
                    }

                    override fun onFailure(message: String) {
                        findViewById<TextView>(R.id.tv_token).text = message
                        progressBar.visibility = View.GONE
                    }
                }
            )
        }
    }
}