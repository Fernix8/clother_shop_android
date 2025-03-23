package com.example.example_android_pe.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.example_android_pe.dao.UsersDao;
import com.example.example_android_pe.database.AppDatabase;
import com.example.example_android_pe.entity.Users;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsersViewModel extends AndroidViewModel {
    private UsersDao usersDao;
    private LiveData<List<Users>> allUsers;
    private ExecutorService executorService;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    public UsersViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        usersDao = database.usersDao();
        allUsers = usersDao.getAllUsers();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Users>> getAllUsers() {
        return allUsers;
    }

    public LiveData<List<Users>> getUsersByRole(String role) {
        return usersDao.getUsersByRole(role);
    }

    public LiveData<Users> getUserById(int userId) {
        return usersDao.getUserById(userId);
    }

    public LiveData<Users> getUserByUsername(String username) {
        return usersDao.getUserByUsername(username);
    }

    public LiveData<List<Users>> searchUsers(String query) {
        searchQuery.setValue(query);
        return usersDao.searchUsers(query);
    }

    public void insertUser(Users user) {
        executorService.execute(() -> usersDao.insertUser(user));
    }

    public void updateUser(Users user) {
        executorService.execute(() -> usersDao.updateUser(user));
    }

    public void deleteUser(Users user) {
        executorService.execute(() -> usersDao.deleteUser(user));
    }

    public void banUser(int userId) {
        executorService.execute(() -> usersDao.updateUserStatus(userId, "banned"));
    }

    public void activateUser(int userId) {
        executorService.execute(() -> usersDao.updateUserStatus(userId, "active"));
    }

    public Users authenticateUser(String username, String password) {
        return usersDao.authenticateUser(username, password);
    }
}