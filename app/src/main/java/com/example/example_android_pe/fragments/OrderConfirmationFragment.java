package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.example_android_pe.R;

public class OrderConfirmationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_confirmation, container, false);
        
        Button continueShoppingButton = view.findViewById(R.id.continueShoppingButton);
        continueShoppingButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_orderConfirmation_to_shop);
        });
        
        return view;
    }
}