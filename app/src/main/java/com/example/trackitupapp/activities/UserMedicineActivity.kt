package com.example.trackitupapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.R
import com.example.trackitupapp.adapters.MedicineAdapter
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.SimpleCallback
import com.example.trackitupapp.apiServices.Callbacks.UserMedicineCallback
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.dataHolder.UserMedicine

class UserMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_medicine)

        calls = ApiCalls()
        adduserMedicineBtn()
        getUserMedecine()
    }

    fun adduserMedicineBtn()
    {
        var addBtn = findViewById<Button>(R.id.btn_addUserMedicine)

        addBtn.setOnClickListener()
        {
            val intent = Intent(this@UserMedicineActivity, AddUserMedicineActivity::class.java)
            startActivity(intent)
        }
    }

    fun getUserMedecine()
    {
        findViewById<ProgressBar>(R.id.pb_loading).visibility = View.VISIBLE
        calls.callUserMedicine(
            applicationContext,
            object : UserMedicineCallback {
                override fun onSuccess(userMedicineList: List<MedicineResponse>) {
                    UserMedicine.addToList(userMedicineList)
                    fetchMedicineData()
                    findViewById<ProgressBar>(R.id.pb_loading).visibility = View.GONE
                }
                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                    findViewById<ProgressBar>(R.id.pb_loading).visibility = View.GONE
                }
            }
        )
    }

    fun fetchMedicineData()
    {
        val userMedicineRecyclerView = findViewById<RecyclerView>(R.id.rv_medicineHolder)
        val medicineAdapter = MedicineAdapter(this@UserMedicineActivity, UserMedicine.getList())

        userMedicineRecyclerView.adapter = medicineAdapter
    }

    fun refreshRVView()
    {
        val userMedicineRecyclerView = findViewById<RecyclerView>(R.id.rv_medicineHolder)
        val medicineAdapter = MedicineAdapter(this@UserMedicineActivity, UserMedicine.getList())

        userMedicineRecyclerView.adapter = medicineAdapter
    }
}