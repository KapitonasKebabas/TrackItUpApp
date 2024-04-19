package com.example.trackitupapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.R
import com.example.trackitupapp.activities.EditMedicineActivity
import com.example.trackitupapp.activities.UserMedicineActivity
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.SimpleCallback
import com.example.trackitupapp.dataHolder.AprovedMedicine
import com.example.trackitupapp.dataHolder.UserMedicine

class MedicineAdapter (private val context: Context, private val medicine: List<MedicineResponse>) : RecyclerView.Adapter<MedicineAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.tv_name)
        val qty: TextView = itemView.findViewById(R.id.tv_amount)
        val expDate: TextView = itemView.findViewById(R.id.tv_expDate)
        val img: ImageView = itemView.findViewById(R.id.iv_img)
        val deleteBtn: Button = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_drug, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicine = medicine[position]
        holder.itemView.apply {
            holder.name.text    = medicine.medecine_name
            holder.expDate.text = "Expiration Date: ${medicine.exp_date}"
            holder.qty.text     = "Amount:  ${medicine.qty}"

            val photoBase64 = AprovedMedicine.getObjectByPk(medicine.medecine)?.photo
            val decodedBytes = Base64.decode(photoBase64 ?: "", Base64.DEFAULT)
            if(decodedBytes.isNotEmpty()) {
                // Convert byte array to Bitmap
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                holder.img.setImageBitmap(bitmap)
            }
        }
        holder.itemView.setOnClickListener{
            val intent = Intent(context, EditMedicineActivity::class.java)
            intent.putExtra("id", medicine.pk)
            context.startActivity(intent)
        }
        holder.deleteBtn.setOnClickListener()
        {
            val calls = ApiCalls()

            calls.callUserMedicineDelete(
                context,
                medicine,
                object : SimpleCallback {
                    override fun onSuccess(message: String) {
                        Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_LONG).show()
                        UserMedicine.deleteItemFromList(medicine.pk)
                        (context as UserMedicineActivity).refreshRVView()
                    }
                    override fun onFailure(message: String) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                }
            )

        }
    }

    override fun getItemCount(): Int {
        return medicine.size
    }
}