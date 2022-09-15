package com.unho.unhomeals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.unho.unhomeals.data.SharedPf;

public class SettingActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SwitchCompat sw_meal1, sw_meal2, sw_meal3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViewById(R.id.exit_setting).setOnClickListener(view -> finish());

        preferences = getSharedPreferences(SharedPf.NAME, Context.MODE_PRIVATE);

        sw_meal1 = findViewById(R.id.sw_meal1);
        sw_meal2 = findViewById(R.id.sw_meal2);
        sw_meal3 = findViewById(R.id.sw_meal3);

        sw_meal1.setChecked(preferences.getBoolean("notify_meal1", false));
        sw_meal2.setChecked(preferences.getBoolean("notify_meal2", false));
        sw_meal3.setChecked(preferences.getBoolean("notify_meal3", false));

        findViewById(R.id.btn_logout).setOnClickListener(view -> {
            SharedPf.put(this, "UID", "");
            Intent intent = new Intent(this, IntroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("notify_meal1", sw_meal1.isChecked());
        editor.putBoolean("notify_meal2", sw_meal2.isChecked());
        editor.putBoolean("notify_meal3", sw_meal3.isChecked());
        editor.apply();
    }
}