package com.example.trackitupapp.activities


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.trackitupapp.NotificationReceiver
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls
import com.example.trackitupapp.apiServices.Callbacks.MedicineCallback
import com.example.trackitupapp.apiServices.responses.MedicineResponse
import com.example.trackitupapp.dataHolder.UserMedicine
import java.time.LocalDate
import java.util.Calendar

private val REQUEST_CODE_NOTIFICATION_PERMISSION = 1
class EditMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls
    private lateinit var expirationEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_medicine)

        calls = ApiCalls()


        val medicineId = intent.getIntExtra("id", -1)
        if (medicineId != -1) {
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.edit_medicine, null)

            val amountEditText: EditText = dialogView.findViewById(R.id.editAmount)
            expirationEditText = dialogView.findViewById(R.id.editExpirationDate)
            val editSwitch: Switch = dialogView.findViewById(R.id.editSwitch)

            val medicine = UserMedicine.getObjectByPk(medicineId)!!

            amountEditText.setText(medicine.qty.toString())
            expirationEditText.setText(medicine.exp_date)
            editSwitch.isChecked = medicine.is_shared
            val dialog = AlertDialog.Builder(this)
                .setTitle("Redaguoti vaisto duomenis")
                .setView(dialogView)
                .setPositiveButton("Išsaugoti", null)
                .setNegativeButton("Atšaukti") { _, _ ->
                    finish()
                }
                    
                .create()


            val btnNotification: Button = dialogView.findViewById(R.id.btnNotification)
            btnNotification.setOnClickListener {
                // Inflate the custom layout for the new dialog
                val newDialogView = layoutInflater.inflate(R.layout.notification_medicine, null)

                // Access the input fields
                val startTimeField: EditText = newDialogView.findViewById(R.id.startTimeField)
                val timesPerDayField: EditText = newDialogView.findViewById(R.id.timesPerDayField)
                val gapBetweenField: EditText = newDialogView.findViewById(R.id.gapBetweenField)
                val intervalField: EditText = newDialogView.findViewById(R.id.intervalField)

                // Create and show the dialog
                val newDialog = AlertDialog.Builder(this)
                    .setTitle(medicine.medecine_name)
                    .setView(newDialogView)
                    .setPositiveButton("Gerai") { _, _ ->
                        // Validate and process input
                        val startTime = startTimeField.text.toString()
                        val timesPerDay = timesPerDayField.text.toString().toIntOrNull()
                        val gapBetween = gapBetweenField.text.toString().toIntOrNull()
                        val interval = intervalField.text.toString().toIntOrNull()

                        val errors = mutableListOf<String>()

                        if (startTime.isEmpty()) {
                            errors.add("Pradžios laikas negali būti tuščias")
                        }
                        if (timesPerDay == null || timesPerDay < 1) {
                            errors.add("Kartai per diena turi būti daugiau arba lygu 1")
                        }
                        if (gapBetween == null || gapBetween < 1) {
                            errors.add("Tarpas tarp vaistų turi būti daugiau arba lygu 1")
                        }
                        if (interval == null || interval < 1) {
                            errors.add("Dienų sakičius turi būti daugiau arba lygu 1")
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                                != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this,
                                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                                    REQUEST_CODE_NOTIFICATION_PERMISSION)
                            }
                        }


                        if (errors.isNotEmpty()) {
                            Toast.makeText(applicationContext, errors.joinToString("\n"), Toast.LENGTH_LONG).show()
                        } else {

                            scheduleNotifications(medicine.medecine_name, startTime, timesPerDay!!, gapBetween!!, interval!!)
                        }
                    }
                    .setNegativeButton("Atšaukti") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                startTimeField.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                    val currentMinute = calendar.get(Calendar.MINUTE)

                    TimePickerDialog(
                        this,
                        { _, selectedHour, selectedMinute ->
                            // Format the time as HH:mm and set it to the EditText
                            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                            startTimeField.setText(formattedTime)
                        },
                        currentHour,
                        currentMinute,
                        true // Set to true for 24-hour format, false for 12-hour format
                    ).show()
                }

                newDialog.show()
            }


            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                    val amountText = amountEditText.text.toString()
                    val expirationDateText = expirationEditText.text.toString()

                    val errors = mutableListOf<String>()

                    if (amountText.isEmpty()) {
                        amountEditText.error = "Amount cannot be empty"
                        errors.add("Amount cannot be empty")
                    } else {
                        val amount = amountText.toInt()
                        if (amount < 1) {
                            amountEditText.error = "Amount must be at least 1"
                            errors.add("Amount must be at least 1")
                        }
                    }

                    if (expirationDateText.isEmpty()) {
                        expirationEditText.error = "Expiration date cannot be empty"
                        errors.add("Expiration date cannot be empty")
                    } else {
                        val expirationDate = LocalDate.parse(expirationDateText)
                        val currentDate = LocalDate.parse(expirationEditText.text.toString())
                        if (expirationDate.isBefore(currentDate)) {
                            expirationEditText.error = "Expiration date cannot be before current date"
                            errors.add("Expiration date cannot be before current date")
                        }
                    }

                    if (errors.isEmpty()) {
                        medicine.qty = amountText.toInt()
                        medicine.exp_date = expirationDateText
                        medicine.is_shared = editSwitch.isChecked
                        updateMedicine(medicine)
                        dialog.dismiss()
                    }
                }
            }

            dialog.show()

        }
    }

    fun updateMedicine(medicine: MedicineResponse) {
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

    fun updateMedicineView(newMedicine: MedicineResponse) {
        Toast.makeText(applicationContext, "Update successfully!", Toast.LENGTH_LONG).show()

        val intent = Intent(this, UserMedicineActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showDatePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val existingDate = expirationEditText.text.toString()
        val dateParts = existingDate.split("-")

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                expirationEditText.setText(selectedDate)
            },
            dateParts[0].toInt(),
            dateParts[1].toInt() - 1,
            dateParts[2].toInt()
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotifications(
        name: String,
        startTime: String,
        timesPerDay: Int,
        gapBetween: Int,
        intervalDays: Int
    ) {
        val calendar = Calendar.getInstance()

        // Parse start time (HH:mm format)
        val timeParts = startTime.split(":")
        val startHour = timeParts[0].toInt()
        val startMinute = timeParts[1].toInt()

        // Set the base start time
        calendar.set(Calendar.HOUR_OF_DAY, startHour)
        calendar.set(Calendar.MINUTE, startMinute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val gapMillis = gapBetween * 60 * 1000L // Convert gap to milliseconds *60
        val intervalMillis = intervalDays * 24 * 60 * 60 * 1000L // Convert days to milliseconds

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (i in 0 until timesPerDay) {
            // Calculate time for each notification
            val triggerTime = calendar.timeInMillis + (i * gapMillis)

            // Create a unique request code for each notification
            val requestCode = i

            // Set up the PendingIntent for the notification
            val intent = Intent(this, NotificationReceiver::class.java)
            intent.putExtra("notificationId", requestCode)
            intent.putExtra("message", "Išgerkite vaistą: $name") // Custom message
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Schedule the notification
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }

        // Schedule daily repetition based on interval
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            intervalMillis,
            PendingIntent.getBroadcast(
                this,
                1000, // Unique request code for interval repetition
                Intent(this, NotificationReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

        Toast.makeText(this, "Pranešimas užregistruotas", Toast.LENGTH_SHORT).show()
    }

}
