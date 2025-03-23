package com.example.example_android_pe.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.example_android_pe.entity.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrder(Order order);

    @Query("SELECT * FROM orders WHERE user_id = :userId ORDER BY order_date DESC")
    LiveData<List<Order>> getOrdersByUserId(int userId);

    @Query("SELECT * FROM orders WHERE id = :orderId")
    LiveData<Order> getOrderById(int orderId);

    @Update
    void updateOrder(Order order);

    @Delete
    void deleteOrder(Order order);
}