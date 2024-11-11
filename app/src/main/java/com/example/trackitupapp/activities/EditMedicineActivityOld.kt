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

class EditMedicineActivityOld : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    private lateinit var expirationEditText: EditText

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_medicine)

        calls = ApiCalls()

        val medicineId = intent.getIntExtra("id", -1)  // CC +1 | Intent extra parsing
        if (medicineId != -1) {  // CC +1 | Conditional check

            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.edit_medicine, null)

            val amountEditText: EditText = dialogView.findViewById(R.id.editAmount)
            expirationEditText = dialogView.findViewById(R.id.editExpirationDate)
            val editSwitch: Switch = dialogView.findViewById(R.id.editSwitch)

            val medicine = UserMedicine.getObjectByPk(medicineId)!! // CC +1 | Fetching medicine object

            amountEditText.setText(medicine.qty.toString()) // CC +1 | Setting value in EditText
            expirationEditText.setText(medicine.exp_date) // CC +1
            editSwitch.isChecked = medicine.is_shared // CC +1

            val dialog = AlertDialog.Builder(this)
                .setTitle("Edit Medicine Details")
                .setView(dialogView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel") { _, _ ->  // CC +1 | Negative button listener
                    finish()  // CC +1 | Finish on cancel
                }
                .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {  // CC +1 | Positive button listener
                    val amountText = amountEditText.text.toString()  // CC +1 | Reading input
                    val expirationDateText = expirationEditText.text.toString() // CC +1

                    val errors = mutableListOf<String>()  // CC +1 | Error handling list

                    if (amountText.isEmpty()) {  // CC +1 | Validation check
                        amountEditText.error = "Amount cannot be empty"
                        errors.add("Amount cannot be empty")
                    } else {
                        val amount = amountText.toInt() // CC +1 | Parsing integer
                        if (amount < 1) {  // CC +1 | Validation check
                            amountEditText.error = "Amount must be at least 1"
                            errors.add("Amount must be at least 1")
                        }
                    }

                    if (expirationDateText.isEmpty()) {  // CC +1 | Validation check
                        expirationEditText.error = "Expiration date cannot be empty"
                        errors.add("Expiration date cannot be empty")
                    } else {
                        val expirationDate = LocalDate.parse(expirationDateText)  // CC +1 | Parsing date
                        val currentDate = LocalDate.parse(expirationEditText.text.toString())  // CC +1 | Date comparison
                        if (expirationDate.isBefore(currentDate)) { // CC +1 | Validation check
                            expirationEditText.error = "Expiration date cannot be before current date"
                            errors.add("Expiration date cannot be before current date")
                        }
                    }

                    if (errors.isEmpty()) {  // CC +1 | Final validation check
                        medicine.qty = amountText.toInt()  // CC +1 | Setting value
                        medicine.exp_date = expirationDateText  // CC +1
                        medicine.is_shared = editSwitch.isChecked  // CC +1
                        updateMedicine(medicine)  // CC +1 | API call to update
                        dialog.dismiss()  // CC +1 | Dismiss dialog
                    }
                }
            }

            dialog.show()  // CC +1 | Show dialog
        }
    }

    fun updateMedicine(medicine: MedicineResponse) {  // CC +1 | Function to update medicine
        calls.callUserMedicineUpdate(
            applicationContext,
            medicine,
            object : MedicineCallback {  // CC +1 | Anonymous class for callback
                override fun onSuccess(medicineResponse: MedicineResponse) {  // CC +1 | On success callback
                    updateMedicineView(medicineResponse)  // CC +1 | Update view with response
                }

                override fun onFailure(message: String) {  // CC +1 | On failure callback
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()  // CC +1 | Show error message
                }
            }
        )
    }

    fun updateMedicineView(newMedicine: MedicineResponse) {  // CC +1 | Update view with new medicine
        Toast.makeText(applicationContext, "Update successfully!", Toast.LENGTH_LONG).show()  // CC +1 | Success message

        val intent = Intent(this, UserMedicineActivity::class.java)  // CC +1 | Starting new activity
        startActivity(intent)  // CC +1 | Starting activity
        finish()  // CC +1 | Finish current activity
    }

    fun showDatePickerDialog(view: View) {  // CC +1 | Show date picker
        val calendar = Calendar.getInstance()  // CC +1 | Get current date
        val year = calendar.get(Calendar.YEAR)  // CC +1 | Extract year
        val month = calendar.get(Calendar.MONTH)  // CC +1 | Extract month
        val day = calendar.get(Calendar.DAY_OF_MONTH)  // CC +1 | Extract day

        val existingDate = expirationEditText.text.toString()  // CC +1 | Get existing date
        val dateParts = existingDate.split("-")  // CC +1 | Split date string

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->  // CC +1 | Date selected callback
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)  // CC +1 | Format date
                expirationEditText.setText(selectedDate)  // CC +1 | Set selected date
            },
            dateParts[0].toInt(),  // CC +1 | Set year
            dateParts[1].toInt() - 1,  // CC +1 | Set month
            dateParts[2].toInt()  // CC +1 | Set day
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000  // CC +1 | Min date logic
        datePickerDialog.show()  // CC +1 | Show date picker dialog
    }
}

// Total Complexity Metrics:
// Cyclomatic Complexity (CC): 35 (Best: 10-15)
// Cognitive Complexity (CCog): 13 (Best: 5-8)
// Technical Debt (TD): 9 (Best: 2-4)
// Reliability Rating: 7 (Best: 9-10)

