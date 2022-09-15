package com.unho.unhomeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.unho.unhomeals.data.SharedPf;

public class LoginActivity extends AppCompatActivity {

    CaptureManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DecoratedBarcodeView dbv_barcode = findViewById(R.id.dbv_scanner);

        manager = new CaptureManager(this, dbv_barcode);
        manager.initializeFromIntent(getIntent(), savedInstanceState);
        manager.decode();

        dbv_barcode.decodeContinuous(result -> {
            String UID = result.toString();
            if (UID.charAt(0) == 'S') {
                SharedPf.put(this, "UID", UID);
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                String message = "잘못된 바코드입니다. 다시 시도해주세요.";
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        manager.onSaveInstanceState(outState);
    }
}