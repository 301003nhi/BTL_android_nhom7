package com.example.project_nhom7_btl.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project_nhom7_btl.adapter.CategoryAdapter;
import com.example.project_nhom7_btl.adapter.ProductAdapter;
import com.example.project_nhom7_btl.data.local.AppDatabase;
import com.example.project_nhom7_btl.data.local.entity.CategoryEntity;
import com.example.project_nhom7_btl.data.local.entity.ProductEntity;
import com.example.project_nhom7_btl.data.prefs.SessionManager;
import com.example.project_nhom7_btl.databinding.ActivityMainBinding;
import com.example.project_nhom7_btl.ui.auth.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SessionManager sessionManager;
    private AppDatabase db;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        setupUI();
        loadData();
    }

    private void setupUI() {
        if (sessionManager.isLoggedIn()) {
            binding.tvWelcome.setText("Xin chào, " + sessionManager.getUsername());
            binding.btnLoginLogout.setText("Đăng xuất");
        } else {
            binding.tvWelcome.setText("Xin chào, Khách");
            binding.btnLoginLogout.setText("Đăng nhập");
        }

        binding.btnLoginLogout.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                sessionManager.logout();
                finish();
                startActivity(getIntent());
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        // Setup Categories Recycler (Horizontal)
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            if (category.getId() == -1) {
                loadAllProducts();
            } else {
                loadProductsByCategory(category.getId());
            }
        });
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategories.setAdapter(categoryAdapter);

        // Setup Products Recycler (Vertical)
        productAdapter = new ProductAdapter(new ArrayList<>(), product -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProducts.setAdapter(productAdapter);
    }

    private void loadData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<CategoryEntity> categories = db.categoryDao().getAllCategories();
            
            // Tạo một danh mục giả "Tất cả" với id = -1
            CategoryEntity allCategory = new CategoryEntity("Tất cả");
            allCategory.setId(-1);
            
            List<CategoryEntity> fullCategoryList = new ArrayList<>();
            fullCategoryList.add(allCategory);
            fullCategoryList.addAll(categories);

            List<ProductEntity> products = db.productDao().getAllProducts();

            runOnUiThread(() -> {
                categoryAdapter.setCategoryList(fullCategoryList);
                productAdapter.setProductList(products);
            });
        });
    }

    private void loadAllProducts() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<ProductEntity> products = db.productDao().getAllProducts();
            runOnUiThread(() -> {
                productAdapter.setProductList(products);
            });
        });
    }

    private void loadProductsByCategory(int categoryId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<ProductEntity> products = db.productDao().getProductsByCategory(categoryId);
            runOnUiThread(() -> {
                productAdapter.setProductList(products);
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager.isLoggedIn()) {
            binding.tvWelcome.setText("Xin chào, " + sessionManager.getUsername());
            binding.btnLoginLogout.setText("Đăng xuất");
        } else {
            binding.tvWelcome.setText("Xin chào, Khách");
            binding.btnLoginLogout.setText("Đăng nhập");
        }
    }
}
