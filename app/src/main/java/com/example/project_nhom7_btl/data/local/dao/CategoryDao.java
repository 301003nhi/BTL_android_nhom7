package com.example.project_nhom7_btl.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project_nhom7_btl.data.local.entity.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(CategoryEntity category);

    @Query("SELECT * FROM categories")
    List<CategoryEntity> getAllCategories();

    @Query("SELECT * FROM categories WHERE id = :id")
    CategoryEntity getCategoryById(int id);
}
