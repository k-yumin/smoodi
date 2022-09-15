package com.unho.unhomeals;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ApplyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        findViewById(R.id.exit_apply).setOnClickListener(view -> finish());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. M. d.");
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String monday = dateFormat.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        String friday = dateFormat.format(calendar.getTime());

        TextView tv_weekday = findViewById(R.id.tv_weekday);
        tv_weekday.setText(monday+" ~ "+friday);
    }
}