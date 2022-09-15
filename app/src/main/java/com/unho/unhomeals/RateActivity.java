package com.unho.unhomeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        findViewById(R.id.exit_rate).setOnClickListener(view -> finish());
    }
}