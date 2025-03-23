package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.example_android_pe.R;
import com.example.example_android_pe.utils.SessionManager;
import com.example.example_android_pe.viewModel.CartViewModel;
import com.example.example_android_pe.viewModel.OrderViewModel;

public class CheckoutFragment extends Fragment {
    private CartViewModel cartViewModel;
    private OrderViewModel orderViewModel;
    private SessionManager sessionManager;
    private TextView totalTextView;
    private EditText addressEditText, cityEditText, stateEditText, zipEditText;
    private Button placeOrderButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());

        // Initialize ViewModels
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        // Set current user ID in view models
        cartViewModel.setCurrentUserId(sessionManager.getUserId());
        orderViewModel.setCurrentUserId(sessionManager.getUserId());

        // Initialize UI components
        totalTextView = view.findViewById(R.id.checkoutTotalTextView);
        addressEditText = view.findViewById(R.id.addressEditText);
        cityEditText = view.findViewById(R.id.cityEditText);
        stateEditText = view.findViewById(R.id.stateEditText);
        zipEditText = view.findViewById(R.id.zipEditText);
        placeOrderButton = view.findViewById(R.id.placeOrderButton);

        // Observe cart total
        cartViewModel.getCartTotal().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                totalTextView.setText(String.format("Total: $%.2f", total));
            } else {
                totalTextView.setText("Total: $0.00");
            }
        });

        // Set up place order button
        placeOrderButton.setOnClickListener(v -> {
            if (validateForm()) {
                String fullAddress = String.format("%s, %s, %s %s",
                        addressEditText.getText().toString(),
                        cityEditText.getText().toString(),
                        stateEditText.getText().toString(),
                        zipEditText.getText().toString());

                Double total = cartViewModel.getCartTotal().getValue();
                if (total == null) total = 0.0;

                orderViewModel.createOrderFromCart(fullAddress, total);

                Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_LONG).show();
                Navigation.findNavController(view).navigate(R.id.action_checkout_to_orderConfirmation);
            }
        });

        return view;
    }

    private boolean validateForm() {
        boolean valid = true;

        if (addressEditText.getText().toString().trim().isEmpty()) {
            addressEditText.setError("Address is required");
            valid = false;
        }

        if (cityEditText.getText().toString().trim().isEmpty()) {
            cityEditText.setError("City is required");
            valid = false;
        }

        if (stateEditText.getText().toString().trim().isEmpty()) {
            stateEditText.setError("State is required");
            valid = false;
        }

        if (zipEditText.getText().toString().trim().isEmpty()) {
            zipEditText.setError("ZIP code is required");
            valid = false;
        }

        return valid;
    }
}