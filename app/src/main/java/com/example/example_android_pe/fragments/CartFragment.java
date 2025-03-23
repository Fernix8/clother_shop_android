package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_android_pe.R;
import com.example.example_android_pe.adapter.CartAdapter;
import com.example.example_android_pe.utils.SessionManager;
import com.example.example_android_pe.viewModel.CartViewModel;

public class CartFragment extends Fragment {
    private CartViewModel cartViewModel;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView totalTextView, emptyCartTextView;
    private Button checkoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());

        // Initialize ViewModel
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Set current user ID in cart view model
        cartViewModel.setCurrentUserId(sessionManager.getUserId());

        // Initialize UI components
        recyclerView = view.findViewById(R.id.cartRecyclerView);
        totalTextView = view.findViewById(R.id.totalTextView);
        emptyCartTextView = view.findViewById(R.id.emptyCartTextView);
        checkoutButton = view.findViewById(R.id.checkoutButton);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CartAdapter(requireContext(), cartViewModel);
        recyclerView.setAdapter(adapter);

        // Observe cart items
        cartViewModel.getCartItems().observe(getViewLifecycleOwner(), cartItems -> {
            adapter.setCartItems(cartItems);

            if (cartItems == null || cartItems.isEmpty()) {
                emptyCartTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                checkoutButton.setEnabled(false);
            } else {
                emptyCartTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                checkoutButton.setEnabled(true);
            }
        });

        // Observe cart total
        cartViewModel.getCartTotal().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                totalTextView.setText(String.format("Total: $%.2f", total));
            } else {
                totalTextView.setText("Total: $0.00");
            }
        });

        // Set up checkout button
        checkoutButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_cart_to_checkout);
        });

        return view;
    }
}