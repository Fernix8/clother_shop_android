package com.example.example_android_pe.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "clothing_item")
public class ClothingItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "size")
    private String size;

    @ColumnInfo(name = "quantity")
    private int quantity;

    // Note: We're not using ForeignKey constraint for now to avoid migration issues
    @ColumnInfo(name = "category_id")
    private Integer categoryId;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "color")
    private String color;

    public ClothingItem() {
    }

    public ClothingItem(String name, double price, String size, int quantity) {
        this.name = name;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
    }

    public ClothingItem(String name, double price, String size, int quantity, Integer categoryId,
                        String description, String imageUrl, String color) {
        this.name = name;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.description = description;
        this.imageUrl = imageUrl;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}