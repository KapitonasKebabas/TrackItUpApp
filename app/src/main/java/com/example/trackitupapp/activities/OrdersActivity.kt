package com.example.trackitupapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.R
import com.example.trackitupapp.adapters.OrderMedicineAdapter
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.OrderCallback
import com.example.trackitupapp.apiServices.Callbacks.OrdersCallback
import com.example.trackitupapp.apiServices.Callbacks.SharedMedicineCallback
import com.example.trackitupapp.apiServices.responses.OrderResponse
import com.example.trackitupapp.apiServices.responses.SharedMedicineResponse
import com.example.trackitupapp.dataHolder.Orders
import com.example.trackitupapp.dataHolder.SharedMedicine
import com.google.android.material.bottomnavigation.BottomNavigationView

class OrdersActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    private var ordersList: List<OrderResponse> = emptyList()
    private lateinit var adapter: OrderMedicineAdapter // Initialize the adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        calls = ApiCalls()

        fetchSharedMedicineData()
        setupFilters()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.my_medicine -> {
                    startActivity(Intent(this@OrdersActivity, UserMedicineActivity::class.java))
                    true
                }
                R.id.share_medicine -> {
                    startActivity(Intent(this@OrdersActivity, SharedMedicineActivity::class.java))
                    true
                }
                R.id.orders -> {
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this@OrdersActivity, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.menu.findItem(R.id.orders).isChecked = true
    }

    private fun fetchSharedMedicineData() {
        calls.callOrders(
            applicationContext,
            object : OrdersCallback {
                override fun onSuccess(ordersResponse: List<OrderResponse>) {
                    Orders.addToList(ordersResponse)
                    fillRecycler()
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun fillRecycler() {
        ordersList = Orders.getList()
        val orderRecyclerView = findViewById<RecyclerView>(R.id.rv_medicineHolder)

        adapter = OrderMedicineAdapter(this@OrdersActivity, ordersList) // Initialize the adapter
        orderRecyclerView.adapter = adapter
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
        val filteredList = ordersList.filter {
            it.medecine_name.contains(name, ignoreCase = true)
        }
        adapter = OrderMedicineAdapter(this, filteredList)
        adapter.notifyDataSetChanged() // Notify adapter about the data change
    }
}
