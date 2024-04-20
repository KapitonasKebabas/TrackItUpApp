package com.example.trackitupapp.adapters

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.R
import com.example.trackitupapp.activities.ChatActivity
import com.example.trackitupapp.activities.EditMedicineActivity
import com.example.trackitupapp.apiServices.responses.SharedMedicineResponse
import com.example.trackitupapp.dataHolder.AprovedMedicine

class SharedMedicineAdapter (private val context: Context, private val medicine: List<SharedMedicineResponse>) : RecyclerView.Adapter<SharedMedicineAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.tv_name_shared)
        val qty: TextView = itemView.findViewById(R.id.tv_amount_shared)
        val img: ImageView = itemView.findViewById(R.id.iv_img)
        val chatBtn: Button = itemView.findViewById(R.id.btn_shared_medicine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.shared_list_item_drug, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicine = medicine[position]
        holder.itemView.apply {
            holder.name.text    = medicine.medecine_name
            holder.qty.text     = "Amount:  ${medicine.shared_qty}"

            val photoBase64 = AprovedMedicine.getObjectByPk(medicine.medecine)?.photo
            val decodedBytes = Base64.decode(photoBase64 ?: "", Base64.DEFAULT)
            if(decodedBytes.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                holder.img.setImageBitmap(bitmap)
            }
        }
        holder.chatBtn.setOnClickListener()
        {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("orderId", 1)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return medicine.size
    }
}