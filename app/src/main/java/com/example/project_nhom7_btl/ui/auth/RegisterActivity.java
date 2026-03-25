package com.example.project_nhom7_btl.ui.auth;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_nhom7_btl.data.local.AppDatabase;
import com.example.project_nhom7_btl.data.local.entity.UserEntity;
import com.example.project_nhom7_btl.databinding.ActivityRegisterBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = AppDatabase.getInstance(this);

        binding.btnRegister.setOnClickListener(v -> {
            String fullName = binding.edtFullName.getText().toString().trim();
            String username = binding.edtUsername.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();

            executorService.execute(() -> {
                UserEntity existingUser = database.userDao().getUserByUsername(username);

                if (existingUser != null) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    database.userDao().insertUser(new UserEntity(username, password, fullName));
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            });
        });
    }
}