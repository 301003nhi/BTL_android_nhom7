package com.example.project_nhom7_btl.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project_nhom7_btl.data.local.dao.CategoryDao;
import com.example.project_nhom7_btl.data.local.dao.ProductDao;
import com.example.project_nhom7_btl.data.local.dao.UserDao;
import com.example.project_nhom7_btl.data.local.entity.CategoryEntity;
import com.example.project_nhom7_btl.data.local.entity.OrderDetailEntity;
import com.example.project_nhom7_btl.data.local.entity.OrderEntity;
import com.example.project_nhom7_btl.data.local.entity.ProductEntity;
import com.example.project_nhom7_btl.data.local.entity.UserEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        UserEntity.class,
        CategoryEntity.class,
        ProductEntity.class,
        OrderEntity.class,
        OrderDetailEntity.class
}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "my_app_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                CategoryDao categoryDao = INSTANCE.categoryDao();
                ProductDao productDao = INSTANCE.productDao();

                // Seed Categories
                categoryDao.insert(new CategoryEntity("Điện thoại"));
                categoryDao.insert(new CategoryEntity("Laptop"));
                categoryDao.insert(new CategoryEntity("Phụ kiện"));

                // Seed Products
                productDao.insert(new ProductEntity("iPhone 15 Pro", 25000000, "Siêu phẩm Apple 2023", 1, "https://example.com/iphone15.jpg"));
                productDao.insert(new ProductEntity("Samsung S24 Ultra", 23000000, "Flagship Samsung", 1, "https://example.com/s24.jpg"));
                productDao.insert(new ProductEntity("MacBook Air M3", 28000000, "Mỏng nhẹ, mạnh mẽ", 2, "https://example.com/macm3.jpg"));
                productDao.insert(new ProductEntity("Dell XPS 13", 30000000, "Laptop cao cấp", 2, "https://example.com/dellxps.jpg"));
                productDao.insert(new ProductEntity("AirPods Pro 2", 5000000, "Tai nghe chống ồn", 3, "https://example.com/airpods.jpg"));
            });
        }
    };
}
