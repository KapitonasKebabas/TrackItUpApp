package com.example.trackitupapp.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.R
import com.example.trackitupapp.adapters.MedicineAdapter
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.UserMedicineCallback
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.constants.Constants
import com.example.trackitupapp.dataHolder.UserMedicine
import com.google.android.material.snackbar.Snackbar
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
        val userMedicineRecyclerView = findViewById<RecyclerView>(R.id.rv_medicineHolder)

        checkExpDate(userMedicineRecyclerView, medicineList)

        val medicineAdapter = MedicineAdapter(this@UserMedicineActivity, medicineList)
        userMedicineRecyclerView.adapter = medicineAdapter
    }

    fun refreshRVView()
    {
        val userMedicineRecyclerView = findViewById<RecyclerView>(R.id.rv_medicineHolder)
        val medicineAdapter = MedicineAdapter(this@UserMedicineActivity, UserMedicine.getList())

        userMedicineRecyclerView.adapter = medicineAdapter
    }

    private fun checkExpDate(userMedicineRecyclerView: RecyclerView, medicineList: List<MedicineResponse>) {
        for (medicineResponse in medicineList) {
            val isExpiring = isExpirationDateWithinAWeek(medicineResponse.exp_date)

            if (isExpiring) {
                val message = "'${medicineResponse.medecine_name}' " + getString(R.string.medicineExpiring)
                showTopSnackbar(userMedicineRecyclerView, message)
            }
        }
    }

    private fun showTopSnackbar(rootView: View, message: String) {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view

        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP

        snackbarView.layoutParams = params

        val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        textView.gravity = Gravity.CENTER_HORIZONTAL

        snackbar.show()
    }

    private fun isExpirationDateWithinAWeek(expirationDate: String): Boolean {
        val currentDate = Calendar.getInstance().time
        val expDate = Constants.DATE_FORMAT.parse(expirationDate)

        val differenceInMilliS = expDate.time - currentDate.time
        val differenceInDays = differenceInMilliS / (1000 * 60 * 60 * 24)

        return differenceInDays <= 7
    }
}