package com.example.example_android_pe.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.example_android_pe.dao.PaymentMethodDao;
import com.example.example_android_pe.database.AppDatabase;
import com.example.example_android_pe.entity.PaymentMethod;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaymentMethodViewModel extends AndroidViewModel {
    private PaymentMethodDao paymentMethodDao;
    private ExecutorService executorService;
    private MutableLiveData<Integer> currentUserId = new MutableLiveData<>();

    public PaymentMethodViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        paymentMethodDao = database.paymentMethodDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void setCurrentUserId(int userId) {
        currentUserId.setValue(userId);
    }

    public LiveData<List<PaymentMethod>> getPaymentMethods() {
        if (currentUserId.getValue() == null) {
            return new MutableLiveData<>();
        }
        return paymentMethodDao.getPaymentMethodsByUserId(currentUserId.getValue());
    }

    public LiveData<PaymentMethod> getDefaultPaymentMethod() {
        if (currentUserId.getValue() == null) {
            return new MutableLiveData<>();
        }
        return paymentMethodDao.getDefaultPaymentMethod(currentUserId.getValue());
    }

    public void addPaymentMethod(PaymentMethod paymentMethod) {
        executorService.execute(() -> {
            if (paymentMethod.isDefault()) {
                paymentMethodDao.clearDefaultPaymentMethods(paymentMethod.getUserId());
            }
            paymentMethodDao.insertPaymentMethod(paymentMethod);
        });
    }

    public void updatePaymentMethod(PaymentMethod paymentMethod) {
        executorService.execute(() -> {
            if (paymentMethod.isDefault()) {
                paymentMethodDao.clearDefaultPaymentMethods(paymentMethod.getUserId());
            }
            paymentMethodDao.updatePaymentMethod(paymentMethod);
        });
    }

    public void deletePaymentMethod(PaymentMethod paymentMethod) {
        executorService.execute(() -> paymentMethodDao.deletePaymentMethod(paymentMethod));
    }

    public void setDefaultPaymentMethod(int paymentMethodId) {
        if (currentUserId.getValue() == null) return;

        executorService.execute(() -> {
            paymentMethodDao.clearDefaultPaymentMethods(currentUserId.getValue());
            paymentMethodDao.setDefaultPaymentMethod(paymentMethodId);
        });
    }
}