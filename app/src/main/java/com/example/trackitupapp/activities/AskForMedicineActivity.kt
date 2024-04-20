package com.example.trackitupapp.activities

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
    private lateinit var aprovedMedicine: AprovedMedecineResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_for_medicine)

        calls = ApiCalls()

        var sharedId = intent.getIntExtra("sharedId", -1)

        if(sharedId == -1)
        {
            finish()
        }

        sharedMedicine = SharedMedicine.getObjectByPk(sharedId)!!
        aprovedMedicine = AprovedMedicine.getObjectByPk(sharedMedicine.medecine)!!


        val photoBase64 = aprovedMedicine.photo
        val decodedBytes = Base64.decode(photoBase64 ?: "", Base64.DEFAULT)
        if (decodedBytes.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            findViewById<ImageView>(R.id.iv_img).setImageBitmap(bitmap)
        }

        findViewById<TextView>(R.id.tv_name_shared).text = aprovedMedicine.name
        findViewById<TextView>(R.id.tv_amount_shared).text = sharedMedicine.shared_qty.toString()


        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.activity_ask_for_medicine, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Ask For Medicine")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                if(findViewById<TextView>(R.id.tv_user_shared).text.isNotEmpty()) {
                    val amountEditText: EditText = dialogView.findViewById(R.id.editAmount)
                    createOrder(amountEditText.text.toString().toInt())
                }
            }
            .setNegativeButton("Cancel") { _, _ ->
                finish()
            }
            .create()

        dialog.show()
    }

    fun createOrder(qty: Int)
    {

        val order = OrderCall(
            sharedMedicine.user_pk,
            sharedMedicine.pk,
            qty,
            3
        )


        calls.callAddOrder(
            applicationContext,
            order,
            object : OrderCallback {
                override fun onSuccess(medicine: OrderResponse) {
                    finish()
                    //TODO("Info")
                }

                override fun onFailure(message: String) {
                    finish()
                    //TODO("Info, maybe change availible qty")
                }

            }
        )
    }
}
