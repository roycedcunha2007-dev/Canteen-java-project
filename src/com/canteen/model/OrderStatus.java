package com.canteen.model;

public enum OrderStatus {
    PLACED("Order Placed", 1, "#f59e0b", "bg-amber-100 text-amber-800 border-amber-300"),
    PREPARING("In Kitchen Preparing", 2, "#3b82f6", "bg-blue-100 text-blue-800 border-blue-300"),
    READY("Ready for Pickup", 3, "#10b981", "bg-emerald-100 text-emerald-800 border-emerald-300"),
    COMPLETED("Completed & Picked Up", 4, "#6b7280", "bg-gray-100 text-gray-800 border-gray-300"),
    CANCELLED("Cancelled", 0, "#ef4444", "bg-red-100 text-red-800 border-red-300");

    private final String displayName;
    private final int step;
    private final String hexColor;
    private final String badgeClass;

    OrderStatus(String displayName, int step, String hexColor, String badgeClass) {
        this.displayName = displayName;
        this.step = step;
        this.hexColor = hexColor;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getStep() {
        return step;
    }

    public String getHexColor() {
        return hexColor;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public static OrderStatus fromString(String text) {
        if (text == null) return PLACED;
        for (OrderStatus s : values()) {
            if (s.name().equalsIgnoreCase(text.trim())) {
                return s;
            }
        }
        return PLACED;
    }
}
