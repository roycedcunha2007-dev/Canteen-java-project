package com.canteen.model;

public enum Category {
    ALL("All Items"),
    BREAKFAST("Breakfast"),
    MEALS("Meals & Lunch"),
    SNACKS("Quick Snacks"),
    BEVERAGES("Beverages & Drinks"),
    DESSERTS("Desserts & Treats");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Category fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return ALL;
        }
        for (Category c : Category.values()) {
            if (c.name().equalsIgnoreCase(text.trim()) || c.displayName.equalsIgnoreCase(text.trim())) {
                return c;
            }
        }
        return ALL;
    }
}
