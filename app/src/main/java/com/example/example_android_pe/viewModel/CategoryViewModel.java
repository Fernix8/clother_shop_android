package com.example.example_android_pe.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.example_android_pe.dao.CategoryDao;
import com.example.example_android_pe.database.AppDatabase;
import com.example.example_android_pe.entity.Category;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    private ExecutorService executorService;

    public CategoryViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategories();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<Category> getCategoryById(int categoryId) {
        return categoryDao.getCategoryById(categoryId);
    }

    public void insertCategory(Category category) {
        executorService.execute(() -> categoryDao.insertCategory(category));
    }

    public void updateCategory(Category category) {
        executorService.execute(() -> categoryDao.updateCategory(category));
    }

    public void deleteCategory(Category category) {
        executorService.execute(() -> categoryDao.deleteCategory(category));
    }
}