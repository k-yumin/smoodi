package com.unho.unhomeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViewById(R.id.exit_setting).setOnClickListener(view -> finish());
    }
}