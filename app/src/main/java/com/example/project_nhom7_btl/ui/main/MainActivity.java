package com.example.project_nhom7_btl.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_nhom7_btl.data.prefs.SessionManager;
import com.example.project_nhom7_btl.databinding.ActivityMainBinding;
import com.example.project_nhom7_btl.ui.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);

        String username = sessionManager.getUsername();
        binding.tvWelcome.setText("Xin chào, " + username);

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}