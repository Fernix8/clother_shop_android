package com.example.example_android_pe.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.example_android_pe.entity.OrderItem;

import java.util.List;

@Dao
public interface OrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrderItem(OrderItem orderItem);

    @Query("SELECT * FROM order_items WHERE order_id = :orderId")
    LiveData<List<OrderItem>> getOrderItemsByOrderId(int orderId);

    @Update
    void updateOrderItem(OrderItem orderItem);

    @Delete
    void deleteOrderItem(OrderItem orderItem);
}