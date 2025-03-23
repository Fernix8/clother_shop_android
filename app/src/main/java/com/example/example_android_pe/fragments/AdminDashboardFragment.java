package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_android_pe.R;
import com.example.example_android_pe.adapter.AdminUserAdapter;
import com.example.example_android_pe.utils.SessionManager;
import com.example.example_android_pe.viewModel.ClothingItemViewModel;
import com.example.example_android_pe.viewModel.OrderViewModel;
import com.example.example_android_pe.viewModel.UsersViewModel;

public class AdminDashboardFragment extends Fragment {
    private SessionManager sessionManager;
    private UsersViewModel usersViewModel;
    private ClothingItemViewModel clothingItemViewModel;
    private OrderViewModel orderViewModel;
    private RecyclerView usersRecyclerView;
    private AdminUserAdapter adapter;
    private TextView totalUsersTextView, totalProductsTextView, totalOrdersTextView;
    private EditText searchEditText;
    private Button searchButton, logoutButton, viewProductsButton, viewOrdersButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());

        // Check if user is admin
        if (!sessionManager.isAdmin()) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_adminDashboard_to_login);
            return view;
        }

        // Initialize ViewModels
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        clothingItemViewModel = new ViewModelProvider(this).get(ClothingItemViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        // Initialize UI components
        totalUsersTextView = view.findViewById(R.id.totalUsersTextView);
        totalProductsTextView = view.findViewById(R.id.totalProductsTextView);
        totalOrdersTextView = view.findViewById(R.id.totalOrdersTextView);
        searchEditText = view.findViewById(R.id.searchUsersEditText);
        searchButton = view.findViewById(R.id.searchUsersButton);
        logoutButton = view.findViewById(R.id.adminLogoutButton);
        viewProductsButton = view.findViewById(R.id.viewProductsButton);
        viewOrdersButton = view.findViewById(R.id.viewOrdersButton);
        usersRecyclerView = view.findViewById(R.id.usersRecyclerView);

        // Set up RecyclerView
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AdminUserAdapter(requireContext(), usersViewModel);
        usersRecyclerView.setAdapter(adapter);

        // Observe users
        usersViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            adapter.setUsers(users);
            totalUsersTextView.setText("Total Users: " + users.size());
        });

        // Observe products
        clothingItemViewModel.getAllClothingItems().observe(getViewLifecycleOwner(), items -> {
            totalProductsTextView.setText("Total Products: " + items.size());
        });

        // Set up search
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                usersViewModel.searchUsers(query).observe(getViewLifecycleOwner(), users -> {
                    adapter.setUsers(users);
                });
            } else {
                usersViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
                    adapter.setUsers(users);
                });
            }
        });

        // Set up logout button
        logoutButton.setOnClickListener(v -> {
            sessionManager.logout();
            Navigation.findNavController(v).navigate(R.id.action_adminDashboard_to_login);
        });

        // Set up view products button
        viewProductsButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_adminDashboard_to_adminProducts);
        });

        // Set up view orders button
        viewOrdersButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_adminDashboard_to_adminOrders);
        });

        return view;
    }
}