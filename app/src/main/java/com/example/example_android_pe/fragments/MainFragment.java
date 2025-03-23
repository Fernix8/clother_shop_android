package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_android_pe.R;
import com.example.example_android_pe.adapter.ClothingItemListAdapter;
import com.example.example_android_pe.entity.ClothingItem;
import com.example.example_android_pe.viewModel.ClothingItemViewModel;

public class MainFragment extends Fragment {
    private ClothingItemViewModel clothingItemViewModel;
    private ClothingItemListAdapter adapter;
    private EditText editItemName, editItemPrice, editItemSize, editItemQuantity;
    private Button btnAdd, btnUpdate;
    private ClothingItem selectedClothingItem = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_content, container, false);

        // Ánh xạ các thành phần UI
        editItemName = view.findViewById(R.id.eItemName);
        editItemPrice = view.findViewById(R.id.eItemPrice);
        editItemSize = view.findViewById(R.id.eItemSize);
        editItemQuantity = view.findViewById(R.id.eItemQuantity);
        btnAdd = view.findViewById(R.id.btnAddItem);
        btnUpdate = view.findViewById(R.id.btnUpdateItem);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Khởi tạo ViewModel
        clothingItemViewModel = new ViewModelProvider(this).get(ClothingItemViewModel.class);
        adapter = new ClothingItemListAdapter(requireContext(), clothingItemViewModel, this);
        recyclerView.setAdapter(adapter);

        // Quan sát danh sách sản phẩm và cập nhật Adapter
        clothingItemViewModel.getAllClothingItems().observe(getViewLifecycleOwner(), clothingItems -> {
            adapter.setClothingItems(clothingItems);
        });

        btnAdd.setOnClickListener(v -> addClothingItem());
        btnUpdate.setOnClickListener(v -> updateClothingItem());

        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_main_to_login);
        });

        return view;
    }

    public ClothingItemViewModel getViewModel() {
        return clothingItemViewModel;
    }

    private void addClothingItem() {
        String itemName = editItemName.getText().toString().trim();
        String itemPrice = editItemPrice.getText().toString().trim();
        String itemSize = editItemSize.getText().toString().trim();
        String itemQuantity = editItemQuantity.getText().toString().trim();

        if (itemName.isEmpty() || itemPrice.isEmpty() || itemSize.isEmpty() || itemQuantity.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        ClothingItem clothingItem = new ClothingItem(itemName, Double.parseDouble(itemPrice), itemSize, Integer.parseInt(itemQuantity));
        clothingItemViewModel.insertClothingItem(clothingItem);
        clearFields();
        Toast.makeText(requireContext(), "Clothing item added successfully", Toast.LENGTH_SHORT).show();
    }

    private void updateClothingItem() {
        if (selectedClothingItem == null) {
            Toast.makeText(requireContext(), "Please select a clothing item to update", Toast.LENGTH_SHORT).show();
            return;
        }

        String itemName = editItemName.getText().toString().trim();
        String itemPrice = editItemPrice.getText().toString().trim();
        String itemSize = editItemSize.getText().toString().trim();
        String itemQuantity = editItemQuantity.getText().toString().trim();

        if (itemName.isEmpty() || itemPrice.isEmpty() || itemSize.isEmpty() || itemQuantity.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedClothingItem.setName(itemName);
        selectedClothingItem.setPrice(Double.parseDouble(itemPrice));
        selectedClothingItem.setSize(itemSize);
        selectedClothingItem.setQuantity(Integer.parseInt(itemQuantity));

        clothingItemViewModel.updateClothingItem(selectedClothingItem);
        clearFields();
        selectedClothingItem = null;
        btnUpdate.setVisibility(View.GONE);
        btnAdd.setVisibility(View.VISIBLE);
        Toast.makeText(requireContext(), "Clothing item updated successfully", Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        editItemName.setText("");
        editItemPrice.setText("");
        editItemSize.setText("");
        editItemQuantity.setText("");
    }

    public void selectClothingItemForUpdate(ClothingItem clothingItem) {
        selectedClothingItem = clothingItem;
        editItemName.setText(clothingItem.getName());
        editItemPrice.setText(String.valueOf(clothingItem.getPrice()));
        editItemSize.setText(clothingItem.getSize());
        editItemQuantity.setText(String.valueOf(clothingItem.getQuantity()));
        btnUpdate.setVisibility(View.VISIBLE);
        btnAdd.setVisibility(View.GONE);
    }

}