package com.example.trackitupapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.R
import com.example.trackitupapp.adapters.MedicineAdapter
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.UserMedicineCallback
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.dataHolder.UserMedicine
import java.text.SimpleDateFormat
import java.util.Calendar

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
        val medicineList = UserMedicine.getList()
        Toast.makeText(this@UserMedicineActivity, "duomenys surinkti", Toast.LENGTH_SHORT).show()

        checkExpDate(medicineList)

        val userMedicineRecyclerView = findViewById<RecyclerView>(R.id.rv_medicineHolder)
        val medicineAdapter = MedicineAdapter(this@UserMedicineActivity, medicineList)
        userMedicineRecyclerView.adapter = medicineAdapter
    }

    fun refreshRVView()
    {
        val userMedicineRecyclerView = findViewById<RecyclerView>(R.id.rv_medicineHolder)
        val medicineAdapter = MedicineAdapter(this@UserMedicineActivity, UserMedicine.getList())

        userMedicineRecyclerView.adapter = medicineAdapter
    }

    private fun checkExpDate(medicineList: List<MedicineResponse>) {
        for (medicineResponse in medicineList) {
            val isExpiring = isExpirationDateWithinAWeek(medicineResponse.exp_date)

            if (isExpiring) {
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.popup_message_top, null)

                val textView = layout.findViewById<TextView>(R.id.popup_message)
                textView.text = "'${medicineResponse.medecine_name}' liko mažiau nei savaitė"

                with (Toast(applicationContext)) {
                    setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
                    duration = Toast.LENGTH_SHORT
                    view = layout
                    show()
                }
            }
        }
    }

    private fun isExpirationDateWithinAWeek(expirationDate: String): Boolean {
        val currentDate = Calendar.getInstance().time

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val expDate = dateFormat.parse(expirationDate)

        val differenceInMilliS = expDate.time - currentDate.time

        val differenceInDays = differenceInMilliS / (1000 * 60 * 60 * 24)

        return differenceInDays < 7
    }
}