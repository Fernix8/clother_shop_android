package com.example.example_android_pe.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.example_android_pe.entity.Users;

import java.util.List;

@Dao
public interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(Users user);

    @Query("SELECT * FROM users")
    LiveData<List<Users>> getAllUsers();

    @Update
    void updateUser(Users user);

    @Delete
    void deleteUser(Users user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    LiveData<Users> getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    LiveData<Users> getUserById(int userId);

    @Query("SELECT * FROM users WHERE role = :role")
    LiveData<List<Users>> getUsersByRole(String role);

    @Query("UPDATE users SET status = :status WHERE id = :userId")
    void updateUserStatus(int userId, String status);

    @Query("SELECT * FROM users WHERE username LIKE '%' || :searchQuery || '%' OR email LIKE '%' || :searchQuery || '%'")
    LiveData<List<Users>> searchUsers(String searchQuery);

    // Direct (non-LiveData) methods for immediate access
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    Users getUserByIdDirect(int userId);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    Users authenticateUser(String username, String password);
}