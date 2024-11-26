package com.example.trackitupapp.activities
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
        val spinner: Spinner = findViewById(R.id.sp_aprovedMedicine)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            aprovedMedicineList.map { AprovedMedicineItem(it.pk, it.name) }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupDatePicker() {
        expirationEditText = findViewById(R.id.editExpirationDate)
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
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
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
            //val editSwitch = 0//findViewById<Switch>(R.id.addSwitch)
            //val shareAmountEditText: EditText = "0"//findViewById(R.id.editShareAmount)

            val amountText = amountEditText.text.toString()
            val expirationDateText = expirationEditText.text.toString()

            val errors = mutableListOf<String>()

            /*if (shareAmountEditText.text.toString() == "") {
                shareAmountEditText.setText("1")
            }*/

            if (amountText.isEmpty() || expirationDateText.isEmpty()) {
                amountEditText.error = "Prašome užpildyti visus laukus."
                errors.add("Prašome užpildyti visus laukus.")
            } else {
                val amount = amountText.toInt()
                if (amount < 1) {
                    amountEditText.error = "Amount must be at least 1"
                    errors.add("Amount must be at least 1")
                }else {
                    val expirationDate = LocalDate.parse(expirationDateText)
                    val currentDate = LocalDate.now()
                    if (expirationDate.isBefore(currentDate)) {
                        expirationEditText.error = "Netinkama galiojimo data."
                        errors.add("Netinkama galiojimo data.")
                    }
                }
            }

            if (errors.isNotEmpty()) {
                val errorMessage = errors.joinToString("\n")
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val shareAmount = 0 //shareAmountEditText.text.toString().toInt()
            val isSwitchChecked = false //editSwitch.isChecked

            val medicineCall = MedicineCall(selectedPk, amountText.toInt(), expirationDateText, isSwitchChecked, shareAmount)
            addMedicine(medicineCall)
        }
    }

    private fun addMedicine(medicine: MedicineCall) {
        calls.callAddUserMedicine(
            applicationContext,
            medicine,
            object : MedicineCallback {
                override fun onSuccess(medicine: MedicineResponse) {
                    Toast.makeText(applicationContext, "Vaistas pridėtas sėkmingai.", Toast.LENGTH_SHORT).show()
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
