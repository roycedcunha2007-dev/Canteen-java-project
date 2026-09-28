package com.canteen.model;

import java.io.Serializable;

public class FoodItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private Category category;
    private double price;
    private String description;
    private boolean available;
    private int prepTimeMinutes;
    private String emoji;

    public FoodItem() {
        this.available = true;
        this.prepTimeMinutes = 10;
        this.emoji = "🍲";
    }

    public FoodItem(String id, String name, Category category, double price, String description, boolean available, int prepTimeMinutes, String emoji) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.available = available;
        this.prepTimeMinutes = prepTimeMinutes;
        this.emoji = emoji != null && !emoji.isEmpty() ? emoji : "🍲";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getPrepTimeMinutes() {
        return prepTimeMinutes;
    }

    public void setPrepTimeMinutes(int prepTimeMinutes) {
        this.prepTimeMinutes = prepTimeMinutes;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getFormattedPrice() {
        return String.format("₹%.2f", price);
    }
}
