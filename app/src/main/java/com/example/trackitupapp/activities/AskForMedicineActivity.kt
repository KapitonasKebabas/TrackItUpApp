package com.example.trackitupapp.activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls

class AskForMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_for_medicine)

        calls = ApiCalls()
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.activity_ask_for_medicine, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Ask For Medicine")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
//                medicine.qty = amountEditText.text.toString().toInt()
//                medicine.exp_date = expirationEditText.text.toString()
//                medicine.is_shared = editSwitch.isChecked
//                updateMedicine(medicine)

            }
            .setNegativeButton("Cancel") { _, _ ->
                finish()
            }
            .create()


    }
}
