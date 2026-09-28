package com.canteen.service;

import com.canteen.model.FoodItem;
import com.canteen.model.OrderItem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CartService {
    private static CartService instance;
    private final MenuService menuService;
    // Maps session token -> List of OrderItems
    private final Map<String, List<OrderItem>> carts = new ConcurrentHashMap<>();

    private CartService() {
        this.menuService = MenuService.getInstance();
    }

    public static synchronized CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    public synchronized List<OrderItem> getCart(String sessionToken) {
        if (sessionToken == null) return Collections.emptyList();
        return carts.computeIfAbsent(sessionToken, k -> new ArrayList<>());
    }

    public synchronized void addToCart(String sessionToken, String foodItemId, int quantity) {
        if (sessionToken == null || foodItemId == null || quantity <= 0) return;
        FoodItem item = menuService.getItemById(foodItemId);
        if (item == null || !item.isAvailable()) return;

        List<OrderItem> cart = carts.computeIfAbsent(sessionToken, k -> new ArrayList<>());
        for (OrderItem orderItem : cart) {
            if (orderItem.getFoodItemId().equals(foodItemId)) {
                orderItem.setQuantity(orderItem.getQuantity() + quantity);
                return;
            }
        }
        cart.add(new OrderItem(item.getId(), item.getName(), item.getPrice(), quantity, item.getEmoji()));
    }

    public synchronized void updateQuantity(String sessionToken, String foodItemId, int quantity) {
        if (sessionToken == null || foodItemId == null) return;
        List<OrderItem> cart = carts.get(sessionToken);
        if (cart == null) return;

        if (quantity <= 0) {
            cart.removeIf(item -> item.getFoodItemId().equals(foodItemId));
        } else {
            for (OrderItem item : cart) {
                if (item.getFoodItemId().equals(foodItemId)) {
                    item.setQuantity(quantity);
                    break;
                }
            }
        }
    }

    public synchronized void removeFromCart(String sessionToken, String foodItemId) {
        if (sessionToken == null || foodItemId == null) return;
        List<OrderItem> cart = carts.get(sessionToken);
        if (cart != null) {
            cart.removeIf(item -> item.getFoodItemId().equals(foodItemId));
        }
    }

    public synchronized void clearCart(String sessionToken) {
        if (sessionToken != null) {
            carts.remove(sessionToken);
        }
    }

    public synchronized double calculateTotal(String sessionToken) {
        List<OrderItem> cart = getCart(sessionToken);
        double total = 0.0;
        for (OrderItem item : cart) {
            total += item.getSubtotal();
        }
        return total;
    }

    public synchronized int getCartItemCount(String sessionToken) {
        List<OrderItem> cart = getCart(sessionToken);
        int count = 0;
        for (OrderItem item : cart) {
            count += item.getQuantity();
        }
        return count;
    }
}
