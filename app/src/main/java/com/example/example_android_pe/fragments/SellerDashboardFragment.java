package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_android_pe.R;
import com.example.example_android_pe.adapter.SellerProductAdapter;
import com.example.example_android_pe.entity.ClothingItem;
import com.example.example_android_pe.utils.SessionManager;
import com.example.example_android_pe.viewModel.ClothingItemViewModel;
import com.example.example_android_pe.viewModel.OrderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SellerDashboardFragment extends Fragment {
    private SessionManager sessionManager;
    private ClothingItemViewModel clothingItemViewModel;
    private OrderViewModel orderViewModel;
    private RecyclerView productsRecyclerView;
    private SellerProductAdapter adapter;
    private TextView totalProductsTextView, lowStockTextView, totalSalesTextView;
    private Button viewOrdersButton, logoutButton;
    private FloatingActionButton addProductButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_dashboard, container, false);

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());

        // Check if user is seller
        if (!sessionManager.isSeller()) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_sellerDashboard_to_login);
            return view;
        }

        // Initialize ViewModels
        clothingItemViewModel = new ViewModelProvider(this).get(ClothingItemViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        // Initialize UI components
        totalProductsTextView = view.findViewById(R.id.totalProductsTextView);
        lowStockTextView = view.findViewById(R.id.lowStockTextView);
        totalSalesTextView = view.findViewById(R.id.totalSalesTextView);
        viewOrdersButton = view.findViewById(R.id.viewOrdersButton);
        logoutButton = view.findViewById(R.id.sellerLogoutButton);
        addProductButton = view.findViewById(R.id.addProductButton);
        productsRecyclerView = view.findViewById(R.id.productsRecyclerView);

        // Set up RecyclerView
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SellerProductAdapter(requireContext(), item -> {
            // Navigate to edit product when item is clicked
            Bundle bundle = new Bundle();
            bundle.putInt("itemId", item.getId());
            Navigation.findNavController(view).navigate(R.id.action_sellerDashboard_to_editProduct, bundle);
        });
        productsRecyclerView.setAdapter(adapter);

        // Observe products
        clothingItemViewModel.getAllClothingItems().observe(getViewLifecycleOwner(), items -> {
            adapter.setClothingItems(items);
            totalProductsTextView.setText("Total Products: " + items.size());

            // Count low stock items
            int lowStockCount = 0;
            for (ClothingItem item : items) {
                if (item.getQuantity() <= 5) {
                    lowStockCount++;
                }
            }
            lowStockTextView.setText("Low Stock Items: " + lowStockCount);
        });

        // Set up add product button
        addProductButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_sellerDashboard_to_addProduct);
        });

        // Set up view orders button
        viewOrdersButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_sellerDashboard_to_sellerOrders);
        });

        // Set up logout button
        logoutButton.setOnClickListener(v -> {
            sessionManager.logout();
            Navigation.findNavController(v).navigate(R.id.action_sellerDashboard_to_login);
        });

        return view;
    }
}