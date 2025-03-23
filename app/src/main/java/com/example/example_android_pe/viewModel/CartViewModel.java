package com.example.example_android_pe.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.example_android_pe.dao.CartItemDao;
import com.example.example_android_pe.dao.ClothingItemDao;
import com.example.example_android_pe.database.AppDatabase;
import com.example.example_android_pe.entity.CartItem;
import com.example.example_android_pe.entity.ClothingItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartViewModel extends AndroidViewModel {
    private CartItemDao cartItemDao;
    private ClothingItemDao clothingItemDao;
    private ExecutorService executorService;
    private MutableLiveData<Integer> currentUserId = new MutableLiveData<>();
    private MediatorLiveData<Double> cartTotal = new MediatorLiveData<>();
    private LiveData<List<CartItem>> cartItems;

    public CartViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        cartItemDao = database.cartItemDao();
        clothingItemDao = database.clothingItemDao();
        executorService = Executors.newSingleThreadExecutor();

        // Initialize cart total with 0
        cartTotal.setValue(0.0);
    }

    public void setCurrentUserId(int userId) {
        currentUserId.setValue(userId);

        // Update cart items when user changes
        if (cartItems != null) {
            cartTotal.removeSource(cartItems);
        }

        cartItems = cartItemDao.getCartItemsByUserId(userId);

        // Recalculate total when cart items change
        cartTotal.addSource(cartItems, items -> {
            calculateCartTotal();
        });
    }

    public LiveData<List<CartItem>> getCartItems() {
        if (currentUserId.getValue() == null) {
            return new MutableLiveData<>();
        }
        return cartItemDao.getCartItemsByUserId(currentUserId.getValue());
    }

    public LiveData<Double> getCartTotal() {
        return cartTotal;
    }

    private void calculateCartTotal() {
        if (currentUserId.getValue() == null) {
            cartTotal.setValue(0.0);
            return;
        }

        executorService.execute(() -> {
            double total = 0.0;
            List<CartItem> items = cartItemDao.getCartItemsByUserIdDirect(currentUserId.getValue());

            for (CartItem item : items) {
                ClothingItem clothingItem = clothingItemDao.getClothingItemByIdDirect(item.getItemId());
                if (clothingItem != null) {
                    total += clothingItem.getPrice() * item.getQuantity();
                }
            }

            cartTotal.postValue(total);
        });
    }

    public LiveData<Integer> getCartItemCount() {
        if (currentUserId.getValue() == null) {
            return new MutableLiveData<>(0);
        }
        return cartItemDao.getCartItemCount(currentUserId.getValue());
    }

    public void addToCart(ClothingItem item, int quantity) {
        if (currentUserId.getValue() == null) return;

        executorService.execute(() -> {
            CartItem cartItem = new CartItem(currentUserId.getValue(), item.getId(), quantity);
            cartItemDao.insertCartItem(cartItem);
            calculateCartTotal();
        });
    }

    public void updateCartItemQuantity(CartItem cartItem, int newQuantity) {
        executorService.execute(() -> {
            cartItem.setQuantity(newQuantity);
            cartItemDao.updateCartItem(cartItem);
            calculateCartTotal();
        });
    }

    public void removeFromCart(CartItem cartItem) {
        executorService.execute(() -> {
            cartItemDao.deleteCartItem(cartItem);
            calculateCartTotal();
        });
    }

    public void clearCart() {
        if (currentUserId.getValue() == null) return;
        executorService.execute(() -> {
            cartItemDao.clearCart(currentUserId.getValue());
            calculateCartTotal();
        });
    }
}