package com.example.trackitupapp.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.AprovedMedicineItem
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.MedicineCallback
import com.example.trackitupapp.apiServices.calls.MedicineCall
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.dataHolder.AprovedMedicine
import com.example.trackitupapp.dataHolder.UserMedicine
import java.time.LocalDate
import java.util.Calendar

class AddUserMedicineActivity : AppCompatActivity() {

    private lateinit var apiCalls: ApiCalls
    private lateinit var expirationEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_medicine)

        apiCalls = ApiCalls()

        setupUIComponents()
    }

    private fun setupUIComponents() {
        setupSpinner()
        setupAddButton()
        setupDatePicker()
    }

    private fun setupSpinner() {
        val approvedMedicines = AprovedMedicine.getList()
        val spinner: Spinner = findViewById(R.id.sp_aprovedMedicine)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            approvedMedicines.map { AprovedMedicineItem(it.pk, it.name) }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupDatePicker() {
        expirationEditText = findViewById(R.id.editExpirationDate)
        expirationEditText.setOnClickListener { showDatePickerDialog() }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                expirationEditText.setText(formatDate(year, month, dayOfMonth))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis() - 1000
            show()
        }
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        return String.format("%04d-%02d-%02d", year, month + 1, day)
    }

    private fun setupAddButton() {
        val addButton = findViewById<Button>(R.id.btn_addUserMedicine)
        addButton.setOnClickListener { handleAddButtonClick() }
    }

    private fun handleAddButtonClick() {
        val selectedMedicine = getSelectedMedicine()
        val amountText = findViewById<EditText>(R.id.editAmount).text.toString()
        val expirationDateText = expirationEditText.text.toString()
        val shareAmountText = findViewById<EditText>(R.id.editShareAmount).text.toString().ifEmpty { "1" }
        val isShareEnabled = findViewById<Switch>(R.id.addSwitch).isChecked

        val errors = validateInputs(amountText, expirationDateText)
        if (errors.isNotEmpty()) {
            showToast(errors.joinToString("\n"))
            return
        }

        val medicineCall = createMedicineCall(selectedMedicine.pk, amountText.toInt(), expirationDateText, isShareEnabled, shareAmountText.toInt())
        addMedicine(medicineCall)
    }

    private fun getSelectedMedicine(): AprovedMedicineItem {
        val medicineSpinner = findViewById<Spinner>(R.id.sp_aprovedMedicine)
        return medicineSpinner.selectedItem as AprovedMedicineItem
    }

    private fun validateInputs(amountText: String, expirationDateText: String): List<String> {
        val errors = mutableListOf<String>()

        if (amountText.isEmpty() || amountText.toIntOrNull() ?: 0 < 1) {
            errors.add("Amount must be at least 1")
        }
        if (expirationDateText.isEmpty() || !isDateValid(expirationDateText)) {
            errors.add("Expiration date must be a valid future date")
        }

        return errors
    }

    private fun isDateValid(dateText: String): Boolean {
        return try {
            val expirationDate = LocalDate.parse(dateText)
            expirationDate.isAfter(LocalDate.now())
        } catch (e: Exception) {
            false
        }
    }

    private fun createMedicineCall(pk: Int, amount: Int, expirationDate: String, isShareEnabled: Boolean, shareAmount: Int): MedicineCall {
        return MedicineCall(pk, amount, expirationDate, isShareEnabled, shareAmount)
    }

    private fun addMedicine(medicine: MedicineCall) {
        apiCalls.callAddUserMedicine(
            applicationContext,
            medicine,
            object : MedicineCallback {
                override fun onSuccess(medicine: MedicineResponse) {
                    UserMedicine.addItemToList(medicine)
                    navigateToUserMedicineActivity()
                }

                override fun onFailure(message: String) {
                    showToast(message)
                    navigateToUserMedicineActivity()
                }
            }
        )
    }

    private fun navigateToUserMedicineActivity() {
        startActivity(Intent(this, UserMedicineActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}
