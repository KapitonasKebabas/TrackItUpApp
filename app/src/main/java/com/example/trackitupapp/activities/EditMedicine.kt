package com.example.trackitupapp.activities
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.R
import com.example.trackitupapp.activities.UserMedicineActivity

class EditMedicine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_medicine)

        // Retrieve medicine data from intent extras
        val medicineId = intent.getIntExtra("id", -1)
        if (medicineId != -1) {
            // Medicine ID is valid, you can use it to fetch or edit medicine data
            // For example, you can fetch medicine data corresponding to this ID from your data source
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.edit_medicine, null)

            val amountEditText: EditText = dialogView.findViewById(R.id.editAmount)
            val expirationEditText: EditText = dialogView.findViewById(R.id.editExpirationDate)
            val editSwitch: Switch = dialogView.findViewById(R.id.editSwitch)

            val medicine = fetchMedicineById(medicineId)

            amountEditText.setText(medicine.amount)
            expirationEditText.setText(medicine.expirationDate)

            editSwitch.isChecked = medicine.shareInformation // Set switch state based on the medicine's information sharing status

            val dialog = AlertDialog.Builder(this   )
                .setTitle("Edit Medicine Details")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    // Save the edited data back to the medicine object
                    medicine.amount = amountEditText.text.toString()
                    medicine.expirationDate = expirationEditText.text.toString()
                    medicine.shareInformation = editSwitch.isChecked
                    notifyDataSetChanged()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    val intent = Intent(this, UserMedicineActivity::class.java)
                    this.startActivity(intent)
                }
                .create()

            dialog.show()
        } else {
        }
    }

    private fun notifyDataSetChanged() {


        val intent = Intent(this, UserMedicineActivity::class.java)
        this.startActivity(intent)
    }

    private fun fetchMedicineById(id: Int): Medicine {
        return Medicine()
    }

    private data class Medicine(
        var amount: String = "0",
        var expirationDate: String = "2024-05-16",
        var shareInformation: Boolean = false
    )
}
