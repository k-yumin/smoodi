package com.unho.unhomeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RankingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        findViewById(R.id.exit_ranking).setOnClickListener(view -> finish());
    }
}