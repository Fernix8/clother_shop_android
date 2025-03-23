package com.example.example_android_pe.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.example_android_pe.dao.UserProfileDao;
import com.example.example_android_pe.database.AppDatabase;
import com.example.example_android_pe.entity.UserProfile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserProfileViewModel extends AndroidViewModel {
    private UserProfileDao userProfileDao;
    private ExecutorService executorService;
    private MutableLiveData<Integer> currentUserId = new MutableLiveData<>();

    public UserProfileViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        userProfileDao = database.userProfileDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void setCurrentUserId(int userId) {
        currentUserId.setValue(userId);
    }

    public LiveData<UserProfile> getUserProfile() {
        if (currentUserId.getValue() == null) {
            return new MutableLiveData<>();
        }
        return userProfileDao.getUserProfileByUserId(currentUserId.getValue());
    }

    public void saveUserProfile(UserProfile userProfile) {
        executorService.execute(() -> userProfileDao.insertUserProfile(userProfile));
    }

    public void updateUserProfile(UserProfile userProfile) {
        executorService.execute(() -> userProfileDao.updateUserProfile(userProfile));
    }
}