package com.example.trackitupapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.SharedMedicineCallback
import com.example.trackitupapp.apiServices.responses.SharedMedicineResponse
import com.example.trackitupapp.dataHolder.SharedMedicine
import com.google.android.material.bottomnavigation.BottomNavigationView

class SharedMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    private var sharedMedicineList: List<SharedMedicineResponse> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_medicine)

        calls = ApiCalls()

        fetchSharedMedicineData()
        setupFilters()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.my_medicine -> {
                    startActivity(Intent(this@SharedMedicineActivity, UserMedicineActivity::class.java))
                    true
                }
                R.id.share_medicine -> {
                    true
                }
                R.id.orders -> {
                    startActivity(Intent(this@SharedMedicineActivity, OrdersActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this@SharedMedicineActivity, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.menu.findItem(R.id.share_medicine).isChecked = true
    }

    private fun fetchSharedMedicineData() {
        calls.callSharedMedicine(applicationContext, object : SharedMedicineCallback {
            override fun onSuccess(medicineResponse: List<SharedMedicineResponse>) {
                SharedMedicine.addToList(medicineResponse)
             fillRecycler()
            }

            override fun onFailure(message: String) {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        fetchSharedMedicineData()
    }

    private fun fillRecycler() {
        // Instead of setting up the adapter, we now navigate to the new layout

        // Initialize buttons
        val saveButton = findViewById<Button>(R.id.btn_med_rez_save)
        val cancelButton = findViewById<Button>(R.id.btn_med_rez_cancle)

        saveButton.setOnClickListener {
            // Handle save action
            Toast.makeText(this, "Save button clicked", Toast.LENGTH_SHORT).show()
        }

        cancelButton.setOnClickListener {
            // Handle cancel action
            Toast.makeText(this, "Cancel button clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupFilters() {
        val search = findViewById<SearchView>(R.id.search_filter)

        search.visibility = android.view.View.VISIBLE

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    //filterByName(it)
                }
                return true
            }
        })
    }

}
