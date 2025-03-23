package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.example_android_pe.R;
import com.example.example_android_pe.entity.Category;
import com.example.example_android_pe.entity.ClothingItem;
import com.example.example_android_pe.utils.SessionManager;
import com.example.example_android_pe.viewModel.CategoryViewModel;
import com.example.example_android_pe.viewModel.ClothingItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddProductFragment extends Fragment {
    private SessionManager sessionManager;
    private ClothingItemViewModel clothingItemViewModel;
    private CategoryViewModel categoryViewModel;
    private EditText nameEditText, priceEditText, sizeEditText, quantityEditText, descriptionEditText, colorEditText, imageUrlEditText;
    private Spinner categorySpinner;
    private Button saveButton, cancelButton;
    private List<Category> categories = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());

        // Check if user is seller
        if (!sessionManager.isSeller()) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_addProduct_to_login);
            return view;
        }

        // Initialize ViewModels
        clothingItemViewModel = new ViewModelProvider(this).get(ClothingItemViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Initialize UI components
        nameEditText = view.findViewById(R.id.productNameEditText);
        priceEditText = view.findViewById(R.id.productPriceEditText);
        sizeEditText = view.findViewById(R.id.productSizeEditText);
        quantityEditText = view.findViewById(R.id.productQuantityEditText);
        descriptionEditText = view.findViewById(R.id.productDescriptionEditText);
        colorEditText = view.findViewById(R.id.productColorEditText);
        imageUrlEditText = view.findViewById(R.id.productImageUrlEditText);
        categorySpinner = view.findViewById(R.id.productCategorySpinner);
        saveButton = view.findViewById(R.id.saveProductButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        // Set up category spinner
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), categoryList -> {
            categories = categoryList;
            List<String> categoryNames = new ArrayList<>();

            for (Category category : categoryList) {
                categoryNames.add(category.getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    categoryNames
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
        });

        // Set up save button
        saveButton.setOnClickListener(v -> saveProduct());

        // Set up cancel button
        cancelButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_addProduct_to_sellerDashboard);
        });

        return view;
    }

    private void saveProduct() {
        String name = nameEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String size = sizeEditText.getText().toString().trim();
        String quantityStr = quantityEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String color = colorEditText.getText().toString().trim();
        String imageUrl = imageUrlEditText.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || size.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(requireContext(), "Name, price, size, and quantity are required", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            // Get selected category
            int categoryPosition = categorySpinner.getSelectedItemPosition();
            Integer categoryId = null;
            if (categoryPosition >= 0 && categoryPosition < categories.size()) {
                categoryId = categories.get(categoryPosition).getId();
            }

            ClothingItem item = new ClothingItem(name, price, size, quantity, categoryId, description, imageUrl, color);
            clothingItemViewModel.insertClothingItem(item);

            Toast.makeText(requireContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.action_addProduct_to_sellerDashboard);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid price or quantity", Toast.LENGTH_SHORT).show();
        }
    }
}