package com.example.example_android_pe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_android_pe.R;
import com.example.example_android_pe.entity.CartItem;
import com.example.example_android_pe.entity.ClothingItem;
import com.example.example_android_pe.viewModel.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems = new ArrayList<>();
    private Context context;
    private CartViewModel cartViewModel;

    public CartAdapter(Context context, CartViewModel cartViewModel) {
        this.context = context;
        this.cartViewModel = cartViewModel;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem, cartViewModel);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setCartItems(List<CartItem> items) {
        this.cartItems = items;
        notifyDataSetChanged();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemName, itemPrice, itemQuantity;
        private Button removeButton, increaseButton, decreaseButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cartItemImageView);
            itemName = itemView.findViewById(R.id.cartItemNameTextView);
            itemPrice = itemView.findViewById(R.id.cartItemPriceTextView);
            itemQuantity = itemView.findViewById(R.id.cartItemQuantityTextView);
            removeButton = itemView.findViewById(R.id.removeItemButton);
            increaseButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseButton = itemView.findViewById(R.id.decreaseQuantityButton);
        }

        public void bind(CartItem cartItem, CartViewModel cartViewModel) {
            // In a real app, you would fetch the ClothingItem details from the database
            // For simplicity, we'll just display the item ID and quantity
            itemName.setText("Item #" + cartItem.getItemId());
            itemPrice.setText("$0.00"); // This would be fetched from the ClothingItem
            itemQuantity.setText(String.valueOf(cartItem.getQuantity()));

            // Set placeholder image
            itemImage.setImageResource(R.drawable.placeholder_image);

            // Set up buttons
            removeButton.setOnClickListener(v -> {
                cartViewModel.removeFromCart(cartItem);
            });

            increaseButton.setOnClickListener(v -> {
                cartViewModel.updateCartItemQuantity(cartItem, cartItem.getQuantity() + 1);
            });

            decreaseButton.setOnClickListener(v -> {
                if (cartItem.getQuantity() > 1) {
                    cartViewModel.updateCartItemQuantity(cartItem, cartItem.getQuantity() - 1);
                } else {
                    cartViewModel.removeFromCart(cartItem);
                }
            });
        }
    }
}