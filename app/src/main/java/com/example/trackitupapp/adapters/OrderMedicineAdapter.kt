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
import com.example.trackitupapp.R
import com.example.trackitupapp.activities.ChatActivity
import com.example.trackitupapp.apiServices.responses.OrderResponse
import com.example.trackitupapp.dataHolder.AprovedMedicine
import com.example.trackitupapp.dataHolder.Statuses

class OrderMedicineAdapter(private val context: Context, private val orders: List<OrderResponse>) : RecyclerView.Adapter<OrderMedicineAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name_shared)
        val qty: TextView = itemView.findViewById(R.id.tv_amount_reserved)
        val user: TextView = itemView.findViewById(R.id.tv_User_sharing)
        val status: TextView = itemView.findViewById(R.id.tv_medicine_status)
        val img: ImageView = itemView.findViewById(R.id.iv_img)
        val chatBtn: Button = itemView.findViewById(R.id.btn_shared_medicine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orders_list_item_drug, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        val status = Statuses.getObjectByPk(order.status)
        holder.name.text = order.medecine_name
        holder.qty.text = "Reserved Amount: ${order.qty}" // Change qty to shared_reserved_qty
        holder.user.text = "User: ${order.user_seller_username}" // Set user information
        holder.status.text= "Status: ${status!!.name}"

        val photoBase64 = AprovedMedicine.getObjectByPk(order.aproved_medecine)?.photo
        val decodedBytes = Base64.decode(photoBase64 ?: "", Base64.DEFAULT)
        if (decodedBytes.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            holder.img.setImageBitmap(bitmap)
        }

        holder.chatBtn.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("orderId", order.pk)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}
