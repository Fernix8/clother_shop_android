package com.example.example_android_pe.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.example_android_pe.dao.CartItemDao;
import com.example.example_android_pe.dao.ClothingItemDao;
import com.example.example_android_pe.dao.OrderDao;
import com.example.example_android_pe.dao.OrderItemDao;
import com.example.example_android_pe.database.AppDatabase;
import com.example.example_android_pe.entity.CartItem;
import com.example.example_android_pe.entity.ClothingItem;
import com.example.example_android_pe.entity.Order;
import com.example.example_android_pe.entity.OrderItem;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderViewModel extends AndroidViewModel {
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;
    private CartItemDao cartItemDao;
    private ClothingItemDao clothingItemDao;
    private ExecutorService executorService;
    private MutableLiveData<Integer> currentUserId = new MutableLiveData<>();

    public OrderViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        orderDao = database.orderDao();
        orderItemDao = database.orderItemDao();
        cartItemDao = database.cartItemDao();
        clothingItemDao = database.clothingItemDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void setCurrentUserId(int userId) {
        currentUserId.setValue(userId);
    }

    public LiveData<List<Order>> getUserOrders() {
        if (currentUserId.getValue() == null) {
            return new MutableLiveData<>();
        }
        return orderDao.getOrdersByUserId(currentUserId.getValue());
    }

    public LiveData<Order> getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }

    public LiveData<List<OrderItem>> getOrderItems(int orderId) {
        return orderItemDao.getOrderItemsByOrderId(orderId);
    }

    public void createOrderFromCart(String shippingAddress, double totalAmount) {
        if (currentUserId.getValue() == null) return;

        executorService.execute(() -> {
            // Create new order
            Order order = new Order(
                    currentUserId.getValue(),
                    new Date().getTime(),
                    totalAmount,
                    "pending",
                    shippingAddress
            );

            long orderId = orderDao.insertOrder(order);

            // Get cart items directly
            List<CartItem> cartItems = cartItemDao.getCartItemsByUserIdDirect(currentUserId.getValue());

            if (cartItems != null && !cartItems.isEmpty()) {
                for (CartItem cartItem : cartItems) {
                    ClothingItem item = clothingItemDao.getClothingItemByIdDirect(cartItem.getItemId());
                    if (item != null) {
                        OrderItem orderItem = new OrderItem(
                                (int) orderId,
                                cartItem.getItemId(),
                                cartItem.getQuantity(),
                                item.getPrice()
                        );
                        orderItemDao.insertOrderItem(orderItem);

                        // Update inventory
                        item.setQuantity(item.getQuantity() - cartItem.getQuantity());
                        clothingItemDao.updateClothingItem(item);
                    }
                }
            }

            // Clear the cart
            cartItemDao.clearCart(currentUserId.getValue());
        });
    }

    public void updateOrderStatus(Order order, String newStatus) {
        executorService.execute(() -> {
            order.setStatus(newStatus);
            orderDao.updateOrder(order);
        });
    }
}