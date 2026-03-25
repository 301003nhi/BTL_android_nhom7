package com.example.project_nhom7_btl.ui.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_nhom7_btl.data.local.AppDatabase;
import com.example.project_nhom7_btl.data.local.entity.ProductEntity;
import com.example.project_nhom7_btl.databinding.ActivityProductDetailBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private AppDatabase db;
    private ProductEntity product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(this);

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId != -1) {
            loadProductDetails(productId);
        } else {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupButtons();
    }

    private void loadProductDetails(int productId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            product = db.productDao().getProductById(productId);
            runOnUiThread(() -> {
                if (product != null) {
                    binding.tvProductNameDetail.setText(product.getName());
                    
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    binding.tvProductPriceDetail.setText("Giá: " + formatter.format(product.getPrice()));
                    
                    binding.tvProductDescDetail.setText(product.getDescription());
                    // Nếu có Glide/Picasso thì load ảnh ở đây
                }
            });
        });
    }

    private void setupButtons() {
        binding.btnAddToCart.setOnClickListener(v -> {
            if (product != null) {
                Toast.makeText(this, "Đã thêm " + product.getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
                // Logic thêm vào giỏ hàng sẽ được cài đặt sau
            }
        });

        binding.btnBuyNow.setOnClickListener(v -> {
            if (product != null) {
                Toast.makeText(this, "Mua ngay: " + product.getName(), Toast.LENGTH_SHORT).show();
                // Logic mua ngay sẽ được cài đặt sau
            }
        });
    }
}
