package com.example.example_android_pe.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_android_pe.R;
import com.example.example_android_pe.entity.ClothingItem;
import com.example.example_android_pe.fragments.MainFragment;
import com.example.example_android_pe.viewModel.ClothingItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClothingItemListAdapter extends RecyclerView.Adapter<ClothingItemListAdapter.ClothingItemViewHolder> {
    private List<ClothingItem> clothingItemList = new ArrayList<>();
    private Context context;
    private ClothingItemViewModel viewModel;
    private MainFragment mainFragment;

    public ClothingItemListAdapter(Context context, ClothingItemViewModel viewModel, MainFragment mainFragment) {
        this.context = context;
        this.viewModel = viewModel;
        this.mainFragment = mainFragment;
    }

    @NonNull
    @Override
    public ClothingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clothing_item_list_item, parent, false);
        return new ClothingItemViewHolder(view, context, viewModel, mainFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothingItemViewHolder holder, int position) {
        ClothingItem clothingItem = clothingItemList.get(position);
        holder.bind(clothingItem);
    }

    @Override
    public int getItemCount() {
        return clothingItemList.size();
    }

    public void setClothingItems(List<ClothingItem> clothingItems) {
        this.clothingItemList = clothingItems;
        notifyDataSetChanged();
    }

    public static class ClothingItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemId, itemName, itemPrice, itemSize, itemQuantity;
        private Button btnDelete, btnEdit;
        private Context context;
        private ClothingItemViewModel viewModel;
        private MainFragment mainFragment;

        public ClothingItemViewHolder(@NonNull View itemView, Context context, ClothingItemViewModel viewModel, MainFragment mainFragment) {
            super(itemView);
            this.context = context;
            this.viewModel = viewModel;
            this.mainFragment = mainFragment;

            itemId = itemView.findViewById(R.id.eItemId);
            itemName = itemView.findViewById(R.id.eItemName);
            itemPrice = itemView.findViewById(R.id.eItemPrice);
            itemSize = itemView.findViewById(R.id.eItemSize);
            itemQuantity = itemView.findViewById(R.id.eItemQuantity);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
            btnEdit = itemView.findViewById(R.id.btnEditItem);
        }

        public void bind(ClothingItem clothingItem) {
            itemId.setText(String.valueOf(clothingItem.getId()));
            itemName.setText(clothingItem.getName());
            itemPrice.setText(String.valueOf(clothingItem.getPrice()));
            itemSize.setText(clothingItem.getSize());
            itemQuantity.setText(String.valueOf(clothingItem.getQuantity()));

            btnDelete.setOnClickListener(v -> {
                viewModel.deleteClothingItem(clothingItem);
            });

            btnEdit.setOnClickListener(v -> {
                if (mainFragment != null) {
                    Log.d("ClothingItemListAdapter", "Calling selectClothingItemForUpdate with item: " + clothingItem.getName());
                    mainFragment.selectClothingItemForUpdate(clothingItem);
                } else {
                    Log.d("ClothingItemListAdapter", "mainFragment is null");
                }
            });
        }
    }
}