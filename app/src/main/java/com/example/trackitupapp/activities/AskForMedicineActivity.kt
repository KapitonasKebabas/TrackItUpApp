package com.example.trackitupapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.ApiCalls

class AskForMedicineActivity : AppCompatActivity() {
    private lateinit var calls: ApiCalls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_for_medicine)

        calls = ApiCalls()


    }
}
