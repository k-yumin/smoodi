package com.smoodi.smoodi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        val tvRateBack: TextView = findViewById(R.id.tvRateBack)
        tvRateBack.setOnClickListener { finish() }
    }
}