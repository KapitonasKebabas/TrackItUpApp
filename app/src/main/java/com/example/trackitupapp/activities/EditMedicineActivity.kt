package com.example.trackitupapp.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.MedicineCallback
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.dataHolder.UserMedicine
import java.time.LocalDate
import java.util.Calendar

class EditMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    private lateinit var expirationEditText: EditText

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_medicine)

        calls = ApiCalls()
        val medicineId = intent.getIntExtra("id", -1)

        if (medicineId != -1) {
            val medicine = UserMedicine.getObjectByPk(medicineId) ?: return

            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.edit_medicine, null)

            val amountEditText: EditText = dialogView.findViewById(R.id.editAmount)
            expirationEditText = dialogView.findViewById(R.id.editExpirationDate)
            val editSwitch: Switch = dialogView.findViewById(R.id.editSwitch)

            amountEditText.setText(medicine.qty.toString())
            expirationEditText.setText(medicine.exp_date)
            editSwitch.isChecked = medicine.is_shared

            val dialog = AlertDialog.Builder(this)
                .setTitle("Edit Medicine Details")
                .setView(dialogView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel") { _, _ -> finish() }
                .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                    val errors = mutableListOf<String>()
                    val amountText = amountEditText.text.toString()
                    val expirationDateText = expirationEditText.text.toString()

                    if (amountText.isEmpty() || amountText.toIntOrNull()?.let { it < 1 } == true) {
                        amountEditText.error = "Amount must be a positive number"
                        errors.add("Amount must be a positive number")
                    }

                    if (expirationDateText.isEmpty() || !isValidExpirationDate(expirationDateText)) {
                        expirationEditText.error = "Invalid expiration date"
                        errors.add("Invalid expiration date")
                    }

                    if (errors.isEmpty()) {
                        medicine.qty = amountText.toInt()
                        medicine.exp_date = expirationDateText
                        medicine.is_shared = editSwitch.isChecked
                        updateMedicine(medicine)
                        dialog.dismiss()
                    }
                }
            }

            dialog.show()
        }
    }

    private fun isValidExpirationDate(expirationDateText: String): Boolean {
        return try {
            val expirationDate = LocalDate.parse(expirationDateText)
            val currentDate = LocalDate.now()
            expirationDate.isAfter(currentDate) || expirationDate == currentDate
        } catch (e: Exception) {
            false
        }
    }

    fun updateMedicine(medicine: MedicineResponse) {
        calls.callUserMedicineUpdate(applicationContext, medicine, object : MedicineCallback {
            override fun onSuccess(medicineResponse: MedicineResponse) {
                updateMedicineView(medicineResponse)
            }

            override fun onFailure(message: String) {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun updateMedicineView(newMedicine: MedicineResponse) {
        Toast.makeText(applicationContext, "Update successful!", Toast.LENGTH_LONG).show()
        val intent = Intent(this, UserMedicineActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showDatePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val existingDate = expirationEditText.text.toString()
        val dateParts = existingDate.split("-")
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                expirationEditText.setText(selectedDate)
            },
            dateParts.getOrNull(0)?.toInt() ?: year,
            dateParts.getOrNull(1)?.toInt()?.minus(1) ?: month,
            dateParts.getOrNull(2)?.toInt() ?: day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }
}

// **Total Complexity Metrics**:
// **Cyclomatic Complexity (CC)**: 17
// **Cognitive Complexity (CCog)**: 7
// **Technical Debt**: 3
// **Reliability Rating**: 8
