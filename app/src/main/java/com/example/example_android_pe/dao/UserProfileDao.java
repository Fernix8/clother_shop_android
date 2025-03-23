package com.example.example_android_pe.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.example_android_pe.entity.UserProfile;

@Dao
public interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserProfile(UserProfile userProfile);

    @Query("SELECT * FROM user_profiles WHERE user_id = :userId")
    LiveData<UserProfile> getUserProfileByUserId(int userId);

    @Update
    void updateUserProfile(UserProfile userProfile);

    @Delete
    void deleteUserProfile(UserProfile userProfile);
}