package com.example.trackitupapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.LogOutCallback
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        calls = ApiCalls()

        val logoutButton = findViewById<Button>(R.id.logoutButton)

        logoutButton.setOnClickListener {
            calls.callLogout(
                applicationContext,
                object : LogOutCallback {
                    override fun onSuccess() {

                        startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
                        finishAffinity()
                    }

                    override fun onFailure(message: String) {
                        startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                }
            )
            val intent = intent
            if (intent != null) {
                val orderId = intent.getStringExtra("orderId")
                // Use the orderId as needed
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.my_medicine -> {
                    startActivity(Intent(this@SettingsActivity, UserMedicineActivity::class.java))
                    true
                }
                R.id.share_medicine -> {
                    startActivity(Intent(this@SettingsActivity, SharedMedicineActivity::class.java))
                    true
                }
                R.id.orders -> {
                    startActivity(Intent(this@SettingsActivity, OrdersActivity::class.java))
                    true
                }
                R.id.settings -> {
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.menu.findItem(R.id.settings).isChecked = true
    }
}
