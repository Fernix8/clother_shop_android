package com.example.example_android_pe.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.example_android_pe.dao.ClothingItemDao;
import com.example.example_android_pe.database.AppDatabase;
import com.example.example_android_pe.entity.ClothingItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClothingItemViewModel extends AndroidViewModel {
    private ClothingItemDao clothingItemDao;
    private LiveData<List<ClothingItem>> allClothingItems;
    private ExecutorService executorService;

    public ClothingItemViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        clothingItemDao = database.clothingItemDao();
        allClothingItems = clothingItemDao.getAllClothingItems();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ClothingItem>> getAllClothingItems() {
        return allClothingItems;
    }

    public void insertClothingItem(ClothingItem clothingItem) {
        executorService.execute(() -> clothingItemDao.insertClothingItem(clothingItem));
    }

    public void updateClothingItem(ClothingItem clothingItem) {
        executorService.execute(() -> clothingItemDao.updateClothingItem(clothingItem));
    }

    public void deleteClothingItem(ClothingItem clothingItem) {
        executorService.execute(() -> clothingItemDao.deleteClothingItem(clothingItem));
    }
}