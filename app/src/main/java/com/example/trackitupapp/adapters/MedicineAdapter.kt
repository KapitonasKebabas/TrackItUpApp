package com.example.trackitupapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.R
import com.example.trackitupapp.dataHolder.AprovedMedicine

class MedicineAdapter (private val context: Context, private val medicine: List<MedicineResponse>) : RecyclerView.Adapter<MedicineAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val expDate: TextView = itemView.findViewById(R.id.tv_amount)
        val qty: TextView = itemView.findViewById(R.id.tv_expDate)
        val img: ImageView = itemView.findViewById(R.id.iv_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_drug, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicine = medicine[position]
        holder.itemView.apply {
            holder.name.text    = medicine.medecine_name
            holder.expDate.text = medicine.exp_date
            holder.qty.text     = medicine.qty.toString()

            val photoBase64 = AprovedMedicine.getObjectByPk(medicine.medecine)?.photo
            val decodedBytes = Base64.decode(photoBase64 ?: "", Base64.DEFAULT)
            if(decodedBytes.isNotEmpty()) {
                // Convert byte array to Bitmap
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                holder.img.setImageBitmap(bitmap)
            }
        }
        /*holder.itemView.setOnClickListener{
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra("id", medicine.pk)
            context.startActivity(intent)
        }*/
    }

    override fun getItemCount(): Int {
        return medicine.size
    }
}