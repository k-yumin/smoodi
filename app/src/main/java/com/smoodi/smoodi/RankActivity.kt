package com.smoodi.smoodi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)

        val tvRankBack: TextView = findViewById(R.id.tvRankBack)
        tvRankBack.setOnClickListener { finish() }
    }
}