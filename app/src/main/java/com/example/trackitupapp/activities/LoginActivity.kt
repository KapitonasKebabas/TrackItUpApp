package com.example.trackitupapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.trackitupapp.managers.RegisterActivity
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

        checkCurrentAuth()
    }

    private fun gotoNextActivity()
    {
        val intent = Intent(this@LoginActivity, UserMedicineActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun btnReg()
    {
        val btnReg = findViewById<Button>(R.id.btn_regActivity)

        btnReg.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun initAfterLoginData()
    {
        calls.callAprovedMedicine(
            applicationContext,
            object : AprovedMedicineCallback {
                override fun onSuccess(aprovedMedicineList: List<AprovedMedecineResponse>) {
                    AprovedMedicine.addToList(aprovedMedicineList)
                    findViewById<ProgressBar>(R.id.pb_loading).visibility = View.GONE
                }
                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                    findViewById<ProgressBar>(R.id.pb_loading).visibility = View.GONE
                }
            }
        )
    }

    fun checkCurrentAuth()
    {
        findViewById<ProgressBar>(R.id.pb_loading).visibility = View.VISIBLE
        calls.callCheckToken(
            applicationContext,
            object : SimpleCallback {
                override fun onSuccess(message: String) {
                    findViewById<ProgressBar>(R.id.pb_loading).visibility = View.GONE
                    gotoNextActivity()
                }

                override fun onFailure(message: String) {
                    btnLogin()
                    btnReg()
                    findViewById<ProgressBar>(R.id.pb_loading).visibility = View.GONE
                }
            })
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
                        initAfterLoginData()
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