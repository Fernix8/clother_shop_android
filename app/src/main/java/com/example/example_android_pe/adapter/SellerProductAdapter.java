package com.example.example_android_pe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_android_pe.R;
import com.example.example_android_pe.entity.ClothingItem;

import java.util.ArrayList;
import java.util.List;

public class SellerProductAdapter extends RecyclerView.Adapter<SellerProductAdapter.ProductViewHolder> {
    private List<ClothingItem> clothingItems = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ClothingItem item);
    }

    public SellerProductAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seller_product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ClothingItem item = clothingItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return clothingItems.size();
    }

    public void setClothingItems(List<ClothingItem> items) {
        this.clothingItems = items;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName, productPrice, productSize, productQuantity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.sellerProductImageView);
            productName = itemView.findViewById(R.id.sellerProductNameTextView);
            productPrice = itemView.findViewById(R.id.sellerProductPriceTextView);
            productSize = itemView.findViewById(R.id.sellerProductSizeTextView);
            productQuantity = itemView.findViewById(R.id.sellerProductQuantityTextView);
        }

        public void bind(ClothingItem item, OnItemClickListener listener) {
            productName.setText(item.getName());
            productPrice.setText(String.format("$%.2f", item.getPrice()));
            productSize.setText("Size: " + item.getSize());
            productQuantity.setText("Quantity: " + item.getQuantity());
            
            // Set image if available
            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                // In a real app, you would use a library like Glide or Picasso to load the image
                // For simplicity, we'll just set a placeholder
                productImage.setImageResource(R.drawable.placeholder_image);
            } else {
                productImage.setImageResource(R.drawable.placeholder_image);
            }
            
            // Highlight low stock items
            if (item.getQuantity() <= 5) {
                productQuantity.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            } else {
                productQuantity.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
            }
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}