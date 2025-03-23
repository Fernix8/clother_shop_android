package com.example.example_android_pe.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.example_android_pe.entity.CartItem;

import java.util.List;

@Dao
public interface CartItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCartItem(CartItem cartItem);

    @Query("SELECT * FROM cart_items WHERE user_id = :userId")
    LiveData<List<CartItem>> getCartItemsByUserId(int userId);

    @Query("SELECT * FROM cart_items WHERE user_id = :userId AND item_id = :itemId")
    LiveData<CartItem> getCartItemByUserAndItemId(int userId, int itemId);

    @Update
    void updateCartItem(CartItem cartItem);

    @Delete
    void deleteCartItem(CartItem cartItem);

    @Query("DELETE FROM cart_items WHERE user_id = :userId")
    void clearCart(int userId);

    // Removed the problematic getCartTotal method

    @Query("SELECT COUNT(*) FROM cart_items WHERE user_id = :userId")
    LiveData<Integer> getCartItemCount(int userId);

    // Add a non-LiveData version to get cart items directly
    @Query("SELECT * FROM cart_items WHERE user_id = :userId")
    List<CartItem> getCartItemsByUserIdDirect(int userId);
}