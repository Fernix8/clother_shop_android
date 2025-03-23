package com.example.example_android_pe.utils;

import android.content.Context;

import com.example.example_android_pe.database.AppDatabase;
import com.example.example_android_pe.entity.Category;
import com.example.example_android_pe.entity.ClothingItem;
import com.example.example_android_pe.entity.Users;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataInitializer {
    private final AppDatabase database;
    private final ExecutorService executorService;

    public DataInitializer(Context context) {
        database = AppDatabase.getInstance(context);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void initializeData() {
        executorService.execute(this::insertSampleData);
    }

    private void insertSampleData() {
        // Add sample users
        insertSampleUsers();

        // Add sample categories
        insertSampleCategories();

        // Add sample clothing items
        insertSampleClothingItems();
    }

    private void insertSampleUsers() {
        // Admin user
        Users admin = new Users("admin", "admin123", "1990-01-01", "1234567890", "admin@example.com", "admin");
        database.usersDao().insertUser(admin);

        // Seller users
        Users seller1 = new Users("seller1", "seller123", "1985-05-15", "9876543210", "seller1@example.com", "seller");
        Users seller2 = new Users("seller2", "seller123", "1988-08-20", "5555555555", "seller2@example.com", "seller");
        database.usersDao().insertUser(seller1);
        database.usersDao().insertUser(seller2);

        // Customer users
        Users customer1 = new Users("customer1", "customer123", "1995-03-10", "1112223333", "customer1@example.com", "customer");
        Users customer2 = new Users("customer2", "customer123", "1992-07-22", "4445556666", "customer2@example.com", "customer");
        Users customer3 = new Users("customer3", "customer123", "1998-11-05", "7778889999", "customer3@example.com", "customer");
        database.usersDao().insertUser(customer1);
        database.usersDao().insertUser(customer2);
        database.usersDao().insertUser(customer3);
    }

    private void insertSampleCategories() {
        Category menCategory = new Category("Men", "Clothing for men");
        Category womenCategory = new Category("Women", "Clothing for women");
        Category kidsCategory = new Category("Kids", "Clothing for kids");
        Category accessoriesCategory = new Category("Accessories", "Fashion accessories");

        database.categoryDao().insertCategory(menCategory);
        database.categoryDao().insertCategory(womenCategory);
        database.categoryDao().insertCategory(kidsCategory);
        database.categoryDao().insertCategory(accessoriesCategory);
    }

    private void insertSampleClothingItems() {
        // Men's clothing
        ClothingItem menShirt1 = new ClothingItem(
                "Men's Casual Shirt",
                29.99,
                "M",
                50,
                1,
                "Comfortable casual shirt for men",
                "https://example.com/images/men_shirt1.jpg",
                "Blue"
        );

        ClothingItem menShirt2 = new ClothingItem(
                "Men's Formal Shirt",
                39.99,
                "L",
                30,
                1,
                "Elegant formal shirt for men",
                "https://example.com/images/men_shirt2.jpg",
                "White"
        );

        ClothingItem menPants1 = new ClothingItem(
                "Men's Jeans",
                49.99,
                "32",
                40,
                1,
                "Classic denim jeans for men",
                "https://example.com/images/men_jeans1.jpg",
                "Dark Blue"
        );

        // Women's clothing
        ClothingItem womenDress1 = new ClothingItem(
                "Women's Summer Dress",
                59.99,
                "S",
                25,
                2,
                "Light and comfortable summer dress",
                "https://example.com/images/women_dress1.jpg",
                "Floral"
        );

        ClothingItem womenTop1 = new ClothingItem(
                "Women's Blouse",
                34.99,
                "M",
                35,
                2,
                "Elegant blouse for women",
                "https://example.com/images/women_blouse1.jpg",
                "Pink"
        );

        ClothingItem womenSkirt1 = new ClothingItem(
                "Women's Skirt",
                44.99,
                "M",
                20,
                2,
                "Stylish skirt for women",
                "https://example.com/images/women_skirt1.jpg",
                "Black"
        );

        // Kids' clothing
        ClothingItem kidsShirt1 = new ClothingItem(
                "Kids' T-Shirt",
                19.99,
                "6",
                60,
                3,
                "Comfortable t-shirt for kids",
                "https://example.com/images/kids_tshirt1.jpg",
                "Green"
        );

        ClothingItem kidsPants1 = new ClothingItem(
                "Kids' Jeans",
                29.99,
                "8",
                45,
                3,
                "Durable jeans for kids",
                "https://example.com/images/kids_jeans1.jpg",
                "Blue"
        );

        // Accessories
        ClothingItem hat1 = new ClothingItem(
                "Sun Hat",
                24.99,
                "One Size",
                30,
                4,
                "Stylish sun hat for summer",
                "https://example.com/images/hat1.jpg",
                "Beige"
        );

        ClothingItem scarf1 = new ClothingItem(
                "Winter Scarf",
                19.99,
                "One Size",
                40,
                4,
                "Warm winter scarf",
                "https://example.com/images/scarf1.jpg",
                "Red"
        );

        // Insert all items
        database.clothingItemDao().insertClothingItem(menShirt1);
        database.clothingItemDao().insertClothingItem(menShirt2);
        database.clothingItemDao().insertClothingItem(menPants1);
        database.clothingItemDao().insertClothingItem(womenDress1);
        database.clothingItemDao().insertClothingItem(womenTop1);
        database.clothingItemDao().insertClothingItem(womenSkirt1);
        database.clothingItemDao().insertClothingItem(kidsShirt1);
        database.clothingItemDao().insertClothingItem(kidsPants1);
        database.clothingItemDao().insertClothingItem(hat1);
        database.clothingItemDao().insertClothingItem(scarf1);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}