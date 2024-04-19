package com.example.trackitupapp.activities


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.MedicineCallback
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.dataHolder.UserMedicine
import java.util.Calendar

class EditMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    private lateinit var expirationEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_medicine)

        calls = ApiCalls()

        val medicineId = intent.getIntExtra("id", -1)
        if (medicineId != -1) {
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.edit_medicine, null)

            val amountEditText: EditText = dialogView.findViewById(R.id.editAmount)
            expirationEditText = dialogView.findViewById(R.id.editExpirationDate)
            val editSwitch: Switch = dialogView.findViewById(R.id.editSwitch)

            val medicine = UserMedicine.getObjectByPk(medicineId)!!

            amountEditText.setText(medicine.qty.toString())
            expirationEditText.setText(medicine.exp_date)
            editSwitch.isChecked = medicine.is_shared

            val dialog = AlertDialog.Builder(this)
                .setTitle("Edit Medicine Details")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    medicine.qty = amountEditText.text.toString().toInt()
                    medicine.exp_date = expirationEditText.text.toString()
                    medicine.is_shared = editSwitch.isChecked
                    updateMedicine(medicine)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    finish()
                }
                .create()

            dialog.show()
        }
    }

    fun updateMedicine(medicine: MedicineResponse) {
        calls.callUserMedicineUpdate(
            applicationContext,
            medicine,
            object : MedicineCallback {
                override fun onSuccess(medicineResponse: MedicineResponse) {
                    updateMedicineView(medicineResponse)
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    fun updateMedicineView(newMedicine: MedicineResponse) {
        Toast.makeText(applicationContext, "Update successfully!", Toast.LENGTH_LONG).show()

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
            dateParts[0].toInt(),
            dateParts[1].toInt() - 1,
            dateParts[2].toInt()
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

}
