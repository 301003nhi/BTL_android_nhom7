package com.example.project_nhom7_btl.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_nhom7_btl.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Luôn vào MainActivity khi khởi động app
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
