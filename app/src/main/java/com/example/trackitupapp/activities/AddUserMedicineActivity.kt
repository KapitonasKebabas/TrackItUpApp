package com.example.trackitupapp.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.google.android.material.textfield.TextInputLayout
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddUserMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    private lateinit var expirationEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_medicine)

        calls = ApiCalls()

        populateSpinner()
        addUserMedicineBtn()
        setupDatePicker()
    }

    private fun populateSpinner() {
        val aprovedMedicineList = AprovedMedicine.getList()
        val spinner: Spinner = findViewById<Spinner>(R.id.sp_aprovedMedicine)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            aprovedMedicineList.map { AprovedMedicineItem(it.pk, it.name) }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupDatePicker() {
        expirationEditText = findViewById<EditText>(R.id.editExpirationDate)
        expirationEditText.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                expirationEditText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun addUserMedicineBtn() {
        val addBtn = findViewById<Button>(R.id.btn_addUserMedicine)
        addBtn.setOnClickListener {
            val aprovedMedicineSpiner = findViewById<Spinner>(R.id.sp_aprovedMedicine)
            val selectedMedicineItem = aprovedMedicineSpiner.selectedItem as AprovedMedicineItem
            val selectedPk = selectedMedicineItem.pk //PK of medicine field

            val amountEditText: EditText = findViewById(R.id.editAmount)
            val expirationEditText: EditText = findViewById(R.id.editExpirationDate)
            val editSwitch = findViewById<Switch>(R.id.addSwitch)
            val shareAmountEditText: EditText = findViewById(R.id.editShareAmount)

            if (shareAmountEditText.text.toString() == "") {
                shareAmountEditText.setText("1")
            }

            if (amountEditText.text.toString() == "") {
                shareAmountEditText.setText("1")
            }

            val expirationDate = expirationEditText.text.toString()
            val amount = amountEditText.text.toString().toInt()
            val shareAmount = shareAmountEditText.text.toString().toInt()
            val isSwitchChecked = editSwitch.isChecked

            val medicineCall = MedicineCall(selectedPk, amount, expirationDate, isSwitchChecked, shareAmount)
            addMedicine(medicineCall)
        }
    }

    private fun addMedicine(medicine: MedicineCall) {
        calls.callAddUserMedicine(
            applicationContext,
            medicine,
            object : MedicineCallback {
                override fun onSuccess(medicine: MedicineResponse) {
                    UserMedicine.addItemToList(medicine)

                    val intent =
                        Intent(this@AddUserMedicineActivity, UserMedicineActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

                    val intent =
                        Intent(this@AddUserMedicineActivity, UserMedicineActivity::class.java)
                    startActivity(intent)
                }
            }
        )
    }
}