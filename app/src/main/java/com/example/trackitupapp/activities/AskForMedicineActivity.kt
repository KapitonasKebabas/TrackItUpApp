package com.example.trackitupapp.activities

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.OrderCallback
import com.example.trackitupapp.apiServices.calls.OrderCall
import com.example.trackitupapp.apiServices.responses.AprovedMedecineResponse
import com.example.trackitupapp.apiServices.responses.OrderResponse
import com.example.trackitupapp.apiServices.responses.SharedMedicineResponse
import com.example.trackitupapp.dataHolder.AprovedMedicine
import com.example.trackitupapp.dataHolder.SharedMedicine

class AskForMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    private lateinit var sharedMedicine: SharedMedicineResponse
    private lateinit var approvedMedicine: AprovedMedecineResponse

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_for_medicine)

        calls = ApiCalls()
        val sharedId = intent.getIntExtra("sharedId", -1)

        if (sharedId == -1) {
            finish()
            return
        }

        loadMedicines(sharedId)
        showMedicineDetails()
        setupOrderDialog()
    }

    private fun loadMedicines(sharedId: Int) {
        sharedMedicine = SharedMedicine.getObjectByPk(sharedId)!!
        approvedMedicine = AprovedMedicine.getObjectByPk(sharedMedicine.medecine)!!
    }

    private fun showMedicineDetails() {
        val photoBase64 = approvedMedicine.photo
        photoBase64?.let {
            val decodedBytes = Base64.decode(it, Base64.DEFAULT)
            if (decodedBytes.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                findViewById<ImageView>(R.id.iv_img).setImageBitmap(bitmap)
            }
        }

        findViewById<TextView>(R.id.tv_name_shared).text = approvedMedicine.name
        findViewById<TextView>(R.id.tv_amount_shared).text = sharedMedicine.shared_qty.toString()
    }

    private fun setupOrderDialog() {
        val dialogView = layoutInflater.inflate(R.layout.activity_ask_for_medicine, null)
        AlertDialog.Builder(this)
            .setTitle("Ask For Medicine")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ -> handleSave(dialogView) }
            .setNegativeButton("Cancel") { _, _ -> finish() }
            .create()
            .show()
    }

    private fun handleSave(dialogView: View) {
        val amountEditText: EditText = dialogView.findViewById(R.id.editAmount)
        val quantity = amountEditText.text.toString().toIntOrNull()

        if (quantity != null && quantity > 0) {
            createOrder(quantity)
        } else {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createOrder(qty: Int) {
        val order = OrderCall(sharedMedicine.user_pk, sharedMedicine.pk, qty, 3)

        calls.callAddOrder(applicationContext, order, object : OrderCallback {
            override fun onSuccess(medicine: OrderResponse) {
                finish()
                Toast.makeText(this@AskForMedicineActivity, "Order created", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(message: String) {
                Toast.makeText(this@AskForMedicineActivity, "Failed to create order", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    /**
     * Validates the quantity input. It should be a positive integer.
     * @param quantity The quantity as a string.
     * @return true if the quantity is valid, false otherwise.
     */
    fun validateQuantity(quantity: String): Boolean {
        return try {
            val qty = quantity.toInt()
            qty > 0
        } catch (e: NumberFormatException) {
            false
        }
    }
}
