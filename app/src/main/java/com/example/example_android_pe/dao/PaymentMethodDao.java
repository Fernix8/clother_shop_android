package com.example.example_android_pe.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.example_android_pe.entity.PaymentMethod;

import java.util.List;

@Dao
public interface PaymentMethodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPaymentMethod(PaymentMethod paymentMethod);

    @Query("SELECT * FROM payment_methods WHERE user_id = :userId")
    LiveData<List<PaymentMethod>> getPaymentMethodsByUserId(int userId);

    @Query("SELECT * FROM payment_methods WHERE user_id = :userId AND is_default = 1 LIMIT 1")
    LiveData<PaymentMethod> getDefaultPaymentMethod(int userId);

    @Update
    void updatePaymentMethod(PaymentMethod paymentMethod);

    @Delete
    void deletePaymentMethod(PaymentMethod paymentMethod);

    @Query("UPDATE payment_methods SET is_default = 0 WHERE user_id = :userId")
    void clearDefaultPaymentMethods(int userId);

    @Query("UPDATE payment_methods SET is_default = 1 WHERE id = :paymentMethodId")
    void setDefaultPaymentMethod(int paymentMethodId);
}