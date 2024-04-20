package com.example.trackitupapp.activities

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.R
import com.example.trackitupapp.adapters.SharedMedicineAdapter
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.UserMedicineCallback
import com.example.trackitupapp.apiServices.responses.MedicineResponse


class SharedMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    private var sharedMedicineList: List<MedicineResponse> = emptyList()
    private lateinit var adapter: SharedMedicineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_medicine)

        calls = ApiCalls()

        val recyclerView = findViewById<RecyclerView>(R.id.rv_sharedMedicineHolder)
        adapter = SharedMedicineAdapter(this, sharedMedicineList)
        recyclerView.adapter = adapter

        fetchSharedMedicineData()
        setupFilters()
    }

    private fun fetchSharedMedicineData() {
        calls.callUserMedicine(applicationContext, object : UserMedicineCallback {
            override fun onSuccess(userMedicineList: List<MedicineResponse>) {
                sharedMedicineList = userMedicineList.filter { it.is_shared }
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(message: String) {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupFilters() {
        val search = findViewById<SearchView>(R.id.search_filter)

        search.visibility = View.VISIBLE

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
            it.medecine_name.contains(name, ignoreCase = true) && it.is_shared
        }
        adapter = SharedMedicineAdapter(this, filteredList)
        adapter.notifyDataSetChanged() // Notify adapter about the data change
    }
}
