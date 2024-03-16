package com.example.trackitupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.trackitupapp.R

class MydrugsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mydrugs)

        val drugListView: ListView = findViewById(R.id.drugListView)

        // Sample drug data
        val drugs = listOf(
            Drug("Paracetamol", R.drawable.paracetamol, "10 tablets"),
            Drug("Aspirin", R.drawable.aspirin, "20 tablets"),
            Drug("Ibuprofen", R.drawable.ibuprofen, "15 tablets"),
            Drug("Omeprazole", R.drawable.omeprazole, "30 capsules")
        )

        val adapter = DrugListAdapter(this, drugs)
        drugListView.adapter = adapter
    }
}
data class Drug(val name: String, val imageResId: Int, val amount: String)
{}
class DrugListAdapter(context: Context, private val drugs: List<Drug>) :
    ArrayAdapter<Drug>(context, 0, drugs) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_drug, parent, false)
        }

        val currentDrug = drugs[position]

        val drugImageView: ImageView = itemView!!.findViewById(R.id.drugImage)
        drugImageView.setImageResource(currentDrug.imageResId)

        val drugNameTextView: TextView = itemView.findViewById(R.id.drugName)
        drugNameTextView.text = currentDrug.name

        val drugAmountTextView: TextView = itemView.findViewById(R.id.drugAmount)
        drugAmountTextView.text = "Amount: ${currentDrug.amount}"

        return itemView
    }
}