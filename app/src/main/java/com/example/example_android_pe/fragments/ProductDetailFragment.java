package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.example_android_pe.R;
import com.example.example_android_pe.entity.ClothingItem;
import com.example.example_android_pe.utils.SessionManager;
import com.example.example_android_pe.viewModel.CartViewModel;
import com.example.example_android_pe.viewModel.ClothingItemViewModel;

public class ProductDetailFragment extends Fragment {
    private ClothingItemViewModel clothingItemViewModel;
    private CartViewModel cartViewModel;
    private SessionManager sessionManager;
    private int itemId;
    private TextView itemName, itemPrice, itemSize, itemQuantity, itemDescription, itemColor;
    private ImageView itemImage;
    private Button addToCartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        // Get item ID from arguments
        if (getArguments() != null) {
            itemId = getArguments().getInt("itemId", -1);
        }

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());

        // Initialize ViewModels
        clothingItemViewModel = new ViewModelProvider(this).get(ClothingItemViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Set current user ID in cart view model
        cartViewModel.setCurrentUserId(sessionManager.getUserId());

        // Initialize UI components
        itemName = view.findViewById(R.id.itemNameTextView);
        itemPrice = view.findViewById(R.id.itemPriceTextView);
        itemSize = view.findViewById(R.id.itemSizeTextView);
        itemQuantity = view.findViewById(R.id.itemQuantityTextView);
        itemDescription = view.findViewById(R.id.itemDescriptionTextView);
        itemColor = view.findViewById(R.id.itemColorTextView);
        itemImage = view.findViewById(R.id.itemImageView);
        addToCartButton = view.findViewById(R.id.addToCartButton);

        // Load item details
//        if (itemId != -1) {
//            clothingItemViewModel.getClothingItemById(itemId).observe(getViewLifecycleOwner(), this::displayItemDetails);
//        }

        return view;
    }

    private void displayItemDetails(ClothingItem item) {
        if (item != null) {
            itemName.setText(item.getName());
            itemPrice.setText(String.format("$%.2f", item.getPrice()));
            itemSize.setText("Size: " + item.getSize());
            itemQuantity.setText("Available: " + item.getQuantity());

            if (item.getDescription() != null) {
                itemDescription.setText(item.getDescription());
            } else {
                itemDescription.setText("No description available");
            }

            if (item.getColor() != null) {
                itemColor.setText("Color: " + item.getColor());
            } else {
                itemColor.setVisibility(View.GONE);
            }

            // Set image if available
            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                // In a real app, you would use a library like Glide or Picasso to load the image
                // For simplicity, we'll just set a placeholder
                itemImage.setImageResource(R.drawable.placeholder_image);
            } else {
                itemImage.setImageResource(R.drawable.placeholder_image);
            }

            // Set up add to cart button
            addToCartButton.setOnClickListener(v -> {
                if (item.getQuantity() > 0) {
                    cartViewModel.addToCart(item, 1);
                    Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Item out of stock", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}