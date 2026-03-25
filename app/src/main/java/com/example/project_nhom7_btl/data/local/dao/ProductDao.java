package com.example.project_nhom7_btl.data.local.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.project_nhom7_btl.data.local.entity.ProductEntity;
import java.util.List;
@Dao
public interface ProductDao {
    @Insert
    void insert(ProductEntity product);
    @Query("SELECT * FROM products")
    List<ProductEntity> getAllProducts();
    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    List<ProductEntity> getProductsByCategory(int categoryId);
    @Query("SELECT * FROM products WHERE id = :id")
    ProductEntity getProductById(int id);
}