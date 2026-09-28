package com.canteen.model;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String foodItemId;
    private String foodName;
    private double unitPrice;
    private int quantity;
    private String emoji;

    public OrderItem() {}

    public OrderItem(String foodItemId, String foodName, double unitPrice, int quantity, String emoji) {
        this.foodItemId = foodItemId;
        this.foodName = foodName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.emoji = emoji != null ? emoji : "🍽️";
    }

    public String getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(String foodItemId) {
        this.foodItemId = foodItemId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public double getSubtotal() {
        return unitPrice * quantity;
    }

    public String getFormattedSubtotal() {
        return String.format("₹%.2f", getSubtotal());
    }

    public String getFormattedUnitPrice() {
        return String.format("₹%.2f", unitPrice);
    }
}
