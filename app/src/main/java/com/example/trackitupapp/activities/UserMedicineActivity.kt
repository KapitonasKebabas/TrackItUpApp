package com.example.trackitupapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.R
import com.example.trackitupapp.adapters.MedicineAdapter
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.UserMedicineCallback
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.constants.Constants
import com.example.trackitupapp.dataHolder.UserMedicine
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class UserMedicineActivity(private val apiCalls: ApiCalls = ApiCalls()) : AppCompatActivity() {

    private lateinit var userMedicineRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var addMedicineButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_medicine)

        initViews()
        setupBottomNavigation()
        setupFilters()
        loadUserMedicines()
    }

    private fun initViews() {
        userMedicineRecyclerView = findViewById(R.id.rv_medicineHolder)
        progressBar = findViewById(R.id.pb_loading)
        addMedicineButton = findViewById(R.id.btn_addUserMedicine)

        addMedicineButton.setOnClickListener {
            startActivity(Intent(this, AddUserMedicineActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.my_medicine -> true
                R.id.share_medicine -> {
                    startActivity(Intent(this, SharedMedicineActivity::class.java))
                    true
                }
                R.id.orders -> {
                    startActivity(Intent(this, OrdersActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.menu.findItem(R.id.my_medicine).isChecked = true
    }

    private fun loadUserMedicines() {
        progressBar.visibility = View.VISIBLE
        apiCalls.callUserMedicine(applicationContext, object : UserMedicineCallback {
            override fun onSuccess(userMedicineList: List<MedicineResponse>) {
                UserMedicine.addToList(userMedicineList)
                updateMedicineAdapter(userMedicineList)
                progressBar.visibility = View.GONE
            }

            override fun onFailure(message: String) {
                showToast(message)
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun updateMedicineAdapter(updatedList: List<MedicineResponse>) {
        val medicineAdapter = MedicineAdapter(this, updatedList)
        userMedicineRecyclerView.adapter = medicineAdapter
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun setupFilters() {
        val prescriptionFilter = findViewById<CheckBox>(R.id.PrescriptionMedicine)
        val search = findViewById<SearchView>(R.id.search_filter)
        val ascRadioButton = findViewById<RadioButton>(R.id.radio_asc)
        val descRadioButton = findViewById<RadioButton>(R.id.radio_desc)

        setupSortListeners(ascRadioButton, descRadioButton)
        setupSearchListener(search)
        setupPrescriptionFilter(prescriptionFilter)
    }

    private fun setupSortListeners(ascRadioButton: RadioButton, descRadioButton: RadioButton) {
        ascRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) sortByDateAscending()
        }

        descRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) sortByDateDescending()
        }
    }

    private fun setupSearchListener(search: SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterByName(it) }
                return true
            }
        })
    }

    private fun setupPrescriptionFilter(prescriptionFilter: CheckBox) {
        prescriptionFilter.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) filterByPrescription() else refreshRVView()
        }
    }

    private fun filterByName(name: String) {
        val filteredList = UserMedicine.getList().filter {
            it.medecine_name.contains(name, ignoreCase = true)
        }
        updateMedicineAdapter(filteredList)
    }

    private fun filterByPrescription() {
        val filteredList = UserMedicine.getList().filter { it.medecine_is_prescription }
        updateMedicineAdapter(filteredList)
    }

    private fun refreshRVView() {
        updateMedicineAdapter(UserMedicine.getList())
    }

    private fun sortByDateAscending() {
        val sortedList = UserMedicine.getList().sortedBy { it.exp_date }
        updateMedicineAdapter(sortedList)
    }

    private fun sortByDateDescending() {
        val sortedList = UserMedicine.getList().sortedByDescending { it.exp_date }
        updateMedicineAdapter(sortedList)
    }

    private fun checkExpDate(userMedicineList: List<MedicineResponse>) {
        userMedicineList.forEach { medicine ->
            if (isExpirationDateWithinAWeek(medicine.exp_date)) {
                showTopSnackbar("'${medicine.medecine_name}' ${getString(R.string.medicineExpiring)}")
            }
        }
    }

    private fun showTopSnackbar(message: String) {
        val snackbar = Snackbar.make(userMedicineRecyclerView, message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackbarView.layoutParams = params

        val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        textView.gravity = Gravity.CENTER_HORIZONTAL
        snackbar.show()
    }

    private fun isExpirationDateWithinAWeek(expirationDate: String): Boolean {
        val currentDate = Calendar.getInstance().time
        val expDate = Constants.DATE_FORMAT.parse(expirationDate)
        val differenceInMilliS = expDate.time - currentDate.time
        val differenceInDays = differenceInMilliS / (1000 * 60 * 60 * 24)
        return differenceInDays <= 7
    }
}
