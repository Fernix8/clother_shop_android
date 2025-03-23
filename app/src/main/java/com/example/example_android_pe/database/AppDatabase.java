package com.example.example_android_pe.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.example_android_pe.dao.CartItemDao;
import com.example.example_android_pe.dao.CategoryDao;
import com.example.example_android_pe.dao.ClothingItemDao;
import com.example.example_android_pe.dao.OrderDao;
import com.example.example_android_pe.dao.OrderItemDao;
import com.example.example_android_pe.dao.PaymentMethodDao;
import com.example.example_android_pe.dao.UserProfileDao;
import com.example.example_android_pe.dao.UsersDao;
import com.example.example_android_pe.entity.CartItem;
import com.example.example_android_pe.entity.Category;
import com.example.example_android_pe.entity.ClothingItem;
import com.example.example_android_pe.entity.Order;
import com.example.example_android_pe.entity.OrderItem;
import com.example.example_android_pe.entity.PaymentMethod;
import com.example.example_android_pe.entity.UserProfile;
import com.example.example_android_pe.entity.Users;

@Database(entities = {
        Users.class,
        ClothingItem.class,
        Category.class,
        CartItem.class,
        Order.class,
        OrderItem.class,
        UserProfile.class,
        PaymentMethod.class
}, version = 5, exportSchema = false)  // Increment version to trigger migration
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UsersDao usersDao();
    public abstract ClothingItemDao clothingItemDao();
    public abstract CategoryDao categoryDao();
    public abstract CartItemDao cartItemDao();
    public abstract OrderDao orderDao();
    public abstract OrderItemDao orderItemDao();
    public abstract UserProfileDao userProfileDao();
    public abstract PaymentMethodDao paymentMethodDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "clothing_shop_database"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}