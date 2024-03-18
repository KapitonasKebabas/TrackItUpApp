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
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.trackitupapp.R
import com.google.android.material.textfield.TextInputEditText

class MydrugsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mydrugs)
/*
        val drugListView: ListView = (R.id.drugListView)

        // Sample drug data
        val drugs = listOf(
            Drug("Paracetamol", R.drawable.paracetamol, "10 tablets","2024.05.06"),
            Drug("Aspirin", R.drawable.aspirin, "20 tablets","2026.08.06"),
            Drug("Ibuprofen", R.drawable.ibuprofen, "15 tablets","2026.08.06"),
            Drug("Omeprazole", R.drawable.omeprazole, "30 capsules","2026.08.06")
        )

        val adapter = DrugListAdapter(this, drugs)
        drugListView.adapter = adapter
    }*/
}/*
data class Drug(val name: String, val imageResId: Int, var amount: String, var expirationDate: String, var shareInformation: Boolean = false)


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

        val drugExpirationTextView: TextView = itemView.findViewById(R.id.drugExpiration)
        drugExpirationTextView.text = "Expiration Date: ${currentDrug.expirationDate}"


        itemView.setOnClickListener {
            EditMedicine(currentDrug)
        }

        return itemView
    }
    private fun EditMedicine(drug: Drug) {
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.edit_medicine, null)
        val amountEditText: TextInputEditText = dialogView.findViewById(R.id.editAmount)
        val expirationEditText: TextInputEditText = dialogView.findViewById(R.id.editExpirationDate)
        val editSwitch: Switch = dialogView.findViewById(R.id.editSwitch)

        amountEditText.setText(drug.amount)
        expirationEditText.setText(drug.expirationDate)

        editSwitch.isChecked = drug.shareInformation // Set switch state based on the drug's information sharing status

        val dialog = AlertDialog.Builder(context)
            .setTitle("Edit Medicine Details")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                drug.amount = amountEditText.text.toString()
                drug.expirationDate = expirationEditText.text.toString()
                drug.shareInformation = editSwitch.isChecked // Save the switch state
                notifyDataSetChanged()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }
*/
}