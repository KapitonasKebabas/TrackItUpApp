package com.example.trackitupapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.R
import com.example.trackitupapp.adapters.SharedMedicineAdapter
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.SharedMedicineCallback
import com.example.trackitupapp.apiServices.responses.SharedMedicineResponse
import com.example.trackitupapp.dataHolder.SharedMedicine
import com.google.android.material.bottomnavigation.BottomNavigationView

class SharedMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    private var sharedMedicineList: List<SharedMedicineResponse> = emptyList()
    private lateinit var adapter: SharedMedicineAdapter

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

    private fun fillRecycler()
    {
        sharedMedicineList = SharedMedicine.getList()
        val sharedMedicineRecyclerView = findViewById<RecyclerView>(R.id.rv_sharedMedicineHolder)

        adapter = SharedMedicineAdapter(this@SharedMedicineActivity, sharedMedicineList)
        sharedMedicineRecyclerView.adapter = adapter
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
                    filterByName(it)
                }
                return true
            }
        })
    }

    private fun filterByName(name: String) {
        val filteredList = sharedMedicineList.filter {
            it.medecine_name.contains(name, ignoreCase = true)
        }
        adapter = SharedMedicineAdapter(this, filteredList)
        adapter.notifyDataSetChanged() // Notify adapter about the data change
    }
}