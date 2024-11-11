package com.example.trackitupapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.AprovedMedicineCallback
import com.example.trackitupapp.apiServices.Callbacks.LoginCallback
import com.example.trackitupapp.apiServices.Callbacks.StatusesCallback
import com.example.trackitupapp.apiServices.responses.AprovedMedecineResponse
import com.example.trackitupapp.apiServices.responses.StatusResponse
import com.example.trackitupapp.dataHolder.AprovedMedicine
import com.example.trackitupapp.dataHolder.Statuses
import com.example.trackitupapp.enums.ProfilePreferences
import com.example.trackitupapp.managers.TokenManager
import com.example.trackitupapp.managers.UserManager
import java.util.Timer
import java.util.TimerTask

class LoginActivity(private val apiCalls: ApiCalls = ApiCalls()) : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var questionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        initListeners()

        if (isUserAlreadyLoggedIn()) {
            logIn()
        }
    }

    private fun initViews() {
        progressBar = findViewById(R.id.pb_loading)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.btn_login)
        registerButton = findViewById(R.id.btn_regActivity)
        questionTextView = findViewById(R.id.question)

        questionTextView.movementMethod = LinkMovementMethod.getInstance()
        setResetPasswordLink()
    }

    private fun initListeners() {
        loginButton.setOnClickListener { logIn() }
        registerButton.setOnClickListener { navigateToRegisterActivity() }
    }

    private fun isUserAlreadyLoggedIn(): Boolean {
        val username = UserManager().getUserDataByFieldString(applicationContext, ProfilePreferences.Username.toString())
        val password = UserManager().getUserDataByFieldString(applicationContext, ProfilePreferences.Password.toString())
        return username.isNotBlank() && password.isNotBlank()
    }

    private fun setResetPasswordLink() {
        val spannableString = SpannableString(getString(R.string.login_psw_forget))
        val resetPasswordClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Handle "Reset password" click
            }
        }

        val startIndex = spannableString.indexOf("Reset password")
        val endIndex = startIndex + "Reset password".length
        spannableString.setSpan(resetPasswordClickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        questionTextView.text = spannableString
    }

    private fun logIn() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (username.isBlank() || password.isBlank()) {
            authDenied("Abu laukai turi buti uzpildyti")
            return
        }

        progressBar.visibility = View.VISIBLE
        apiCalls.callLogIn(applicationContext, username, password, object : LoginCallback {
            override fun onSuccess() {
                handleLoginSuccess(username, password)
            }

            override fun onFailure(message: String) {
                authDenied(message)
            }
        })
    }

    private fun handleLoginSuccess(username: String, password: String) {
        UserManager().saveDataByFieldString(applicationContext, ProfilePreferences.Username.toString(), username)
        UserManager().saveDataByFieldString(applicationContext, ProfilePreferences.Password.toString(), password)
        authorized()
    }

    private fun authorized() {
        startTokenRefreshTask()

        progressBar.visibility = View.VISIBLE
        fetchAprovedMedicineData()
        fetchStatusesData()

        navigateToUserMedicineActivity()
        finish()
    }

    private fun startTokenRefreshTask() {
        val handler = Handler()
        val timer = Timer()
        val refreshTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    try {
                        TokenManager().updateAuthToken(this@LoginActivity)
                    } catch (e: Exception) {
                        // Handle exception if needed
                    }
                }
            }
        }
        timer.schedule(refreshTask, 0, 660000)
    }

    private fun fetchAprovedMedicineData() {
        apiCalls.callAprovedMedicine(applicationContext, object : AprovedMedicineCallback {
            override fun onSuccess(aprovedMedicineList: List<AprovedMedecineResponse>) {
                AprovedMedicine.addToList(aprovedMedicineList)
                progressBar.visibility = View.GONE
            }

            override fun onFailure(message: String) {
                showToast(message)
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun fetchStatusesData() {
        apiCalls.callStatuses(applicationContext, object : StatusesCallback {
            override fun onSuccess(statuses: List<StatusResponse>) {
                Statuses.addToList(statuses)
            }

            override fun onFailure(message: String) {
                // Handle failure if necessary
            }
        })
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToUserMedicineActivity() {
        val intent = Intent(this, UserMedicineActivity::class.java)
        startActivity(intent)
    }

    private fun authDenied(message: String) {
        findViewById<TextView>(R.id.tv_token).text = message
        progressBar.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}
