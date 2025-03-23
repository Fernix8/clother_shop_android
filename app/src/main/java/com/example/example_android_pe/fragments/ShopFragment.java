package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_android_pe.R;
import com.example.example_android_pe.adapter.ProductAdapter;
import com.example.example_android_pe.entity.Category;
import com.example.example_android_pe.entity.ClothingItem;
import com.example.example_android_pe.utils.SessionManager;
import com.example.example_android_pe.viewModel.CartViewModel;
import com.example.example_android_pe.viewModel.CategoryViewModel;
import com.example.example_android_pe.viewModel.ClothingItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShopFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ClothingItemViewModel clothingItemViewModel;
    private CategoryViewModel categoryViewModel;
    private CartViewModel cartViewModel;
    private SessionManager sessionManager;
    private TextView cartCountTextView;
    private Spinner categorySpinner;
    private List<Category> allCategories = new ArrayList<>();
    private List<ClothingItem> allClothingItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());

        // Initialize ViewModels
        clothingItemViewModel = new ViewModelProvider(this).get(ClothingItemViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Set current user ID in cart view model
        cartViewModel.setCurrentUserId(sessionManager.getUserId());

        // Initialize UI components
        recyclerView = view.findViewById(R.id.productsRecyclerView);
        cartCountTextView = view.findViewById(R.id.cartCountTextView);
        categorySpinner = view.findViewById(R.id.categorySpinner);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adapter = new ProductAdapter(requireContext(), item -> {
            // Navigate to product detail when item is clicked
            Bundle bundle = new Bundle();
            bundle.putInt("itemId", item.getId());
            Navigation.findNavController(view).navigate(R.id.action_shop_to_productDetail, bundle);
        });
        recyclerView.setAdapter(adapter);

        // Observe clothing items
        clothingItemViewModel.getAllClothingItems().observe(getViewLifecycleOwner(), clothingItems -> {
            allClothingItems = clothingItems;
            adapter.setClothingItems(clothingItems);
        });

        // Observe cart count
        cartViewModel.getCartItemCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                cartCountTextView.setText(String.valueOf(count));
                cartCountTextView.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
            }
        });

        // Set up category spinner
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            allCategories = categories;
            List<String> categoryNames = new ArrayList<>();
            categoryNames.add("All Categories");

            for (Category category : categories) {
                categoryNames.add(category.getName());
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    categoryNames
            );
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(spinnerAdapter);
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Filter products by category
                if (position == 0) {
                    // "All Categories" selected
                    adapter.setClothingItems(allClothingItems);
                } else if (position <= allCategories.size()) {
                    // Specific category selected - filter in memory instead of using DAO
                    Category selectedCategory = allCategories.get(position - 1);

                    // Filter items that match the selected category
                    // Note: This is a temporary solution until we have proper category_id in the database
                    // In a real app, you would filter by category_id
                    List<ClothingItem> filteredItems = allClothingItems.stream()
                            .filter(item -> item.getCategoryId() != null &&
                                    item.getCategoryId() == selectedCategory.getId())
                            .collect(Collectors.toList());

                    adapter.setClothingItems(filteredItems);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up cart button
        FloatingActionButton cartButton = view.findViewById(R.id.cartButton);
        cartButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_shop_to_cart);
        });

        return view;
    }
}