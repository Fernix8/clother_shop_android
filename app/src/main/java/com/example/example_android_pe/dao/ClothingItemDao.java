package com.example.example_android_pe.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.example_android_pe.entity.ClothingItem;

import java.util.List;

@Dao
public interface ClothingItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClothingItem(ClothingItem clothingItem);

    @Query("SELECT * FROM clothing_item")
    LiveData<List<ClothingItem>> getAllClothingItems();

    @Query("SELECT * FROM clothing_item WHERE id = :id")
    LiveData<ClothingItem> getClothingItemById(int id);

    // Add a non-LiveData version to get clothing item directly
    @Query("SELECT * FROM clothing_item WHERE id = :id")
    ClothingItem getClothingItemByIdDirect(int id);

    @Update
    void updateClothingItem(ClothingItem clothingItem);

    @Delete
    void deleteClothingItem(ClothingItem clothingItem);

    // Search functionality
    @Query("SELECT * FROM clothing_item WHERE name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    LiveData<List<ClothingItem>> searchClothingItems(String searchQuery);

    // Filter by price range
    @Query("SELECT * FROM clothing_item WHERE price BETWEEN :minPrice AND :maxPrice")
    LiveData<List<ClothingItem>> getClothingItemsByPriceRange(double minPrice, double maxPrice);

    // Filter by size
    @Query("SELECT * FROM clothing_item WHERE size = :size")
    LiveData<List<ClothingItem>> getClothingItemsBySize(String size);

    // Filter by color
    @Query("SELECT * FROM clothing_item WHERE color = :color")
    LiveData<List<ClothingItem>> getClothingItemsByColor(String color);

    // Get items with low stock
    @Query("SELECT * FROM clothing_item WHERE quantity <= 5")
    LiveData<List<ClothingItem>> getLowStockItems();
}