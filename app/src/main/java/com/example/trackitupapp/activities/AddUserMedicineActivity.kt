package com.example.trackitupapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import com.example.trackitupapp.AprovedMedicineItem
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.MedicineCallback
import com.example.trackitupapp.apiServices.Callbacks.UserMedicineCallback
import com.example.trackitupapp.apiServices.calls.MedicineCall
import com.example.trackitupapp.apiServices.responses.AprovedMedecineResponse
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.dataHolder.AprovedMedicine
import com.example.trackitupapp.dataHolder.UserMedicine
import com.toptoche.searchablespinnerlibrary.SearchableSpinner

class AddUserMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_medicine)

        calls = ApiCalls()

        populateSpinner()
        addUserMedicineBtn()
    }

    private fun populateSpinner() {
        val aprovedMedecineList = AprovedMedicine.getList()
        val spinner: Spinner = findViewById<Spinner>(R.id.sp_aprovedMedicine)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            aprovedMedecineList.map { AprovedMedicineItem(it.pk, it.name) }
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun addUserMedicineBtn()
    {
        val addBtn = findViewById<Button>(R.id.btn_addUserMedicine)

        addBtn.setOnClickListener()
        {
            val aprovedMedicineSpiner = findViewById<Spinner>(R.id.sp_aprovedMedicine)
            val selectedMedicineItem = aprovedMedicineSpiner.selectedItem as AprovedMedicineItem
            val selectedPk = selectedMedicineItem.pk //PK of medicine field
        }
    }

    fun addMedicine(medicine: MedicineCall)
    {
        calls.callAddUserMedicine(
            applicationContext,
            medicine,
            object : MedicineCallback {
                override fun onSuccess(medicine: MedicineResponse) {
                    UserMedicine.addItemToList(medicine)

                    val intent = Intent(this@AddUserMedicineActivity, UserMedicineActivity::class.java)
                    startActivity(intent)
                }
                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

                    val intent = Intent(this@AddUserMedicineActivity, UserMedicineActivity::class.java)
                    startActivity(intent)
                }
            }
        )
    }
}