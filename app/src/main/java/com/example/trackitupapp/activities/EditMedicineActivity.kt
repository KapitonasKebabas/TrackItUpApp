package com.example.trackitupapp.activities
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.MedicineCallback
import com.example.trackitupapp.apiServices.Callbacks.SimpleCallback
import com.example.trackitupapp.apiServices.responses.AprovedMedecineResponse
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.dataHolder.AprovedMedicine
import com.example.trackitupapp.dataHolder.UserMedicine

class EditMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_medicine)

        calls = ApiCalls()

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

            val medicine = UserMedicine.getObjectByPk(medicineId)!!

                amountEditText.setText(medicine.qty.toString())
                expirationEditText.setText(medicine.exp_date)

            editSwitch.isChecked = medicine.is_shared // Set switch state based on the medicine's information sharing status

            val dialog = AlertDialog.Builder(this   )
                .setTitle("Edit Medicine Details")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    // Save the edited data back to the medicine object
                    medicine.qty = amountEditText.text.toString().toInt()
                    medicine.exp_date = expirationEditText.text.toString()
                    medicine.is_shared = editSwitch.isChecked
                    updateMedicine(medicine)
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

    private fun updateMedicine(medicine: MedicineResponse) {

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

    fun updateMedicineView(newMedicine: MedicineResponse)
    {
        var medicine = UserMedicine.getObjectByPk(newMedicine.pk)!!
        medicine = newMedicine

        Toast.makeText(applicationContext, "Update successfully!", Toast.LENGTH_LONG).show()

        val intent = Intent(this, UserMedicineActivity::class.java)
        this.startActivity(intent)
    }

}
