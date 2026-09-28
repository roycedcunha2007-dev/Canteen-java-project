package com.canteen.service;

import com.canteen.model.*;
import com.canteen.storage.DataStore;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OrderService {
    private static OrderService instance;
    private final DataStore dataStore;
    private final CartService cartService;
    private final AtomicInteger orderSequence = new AtomicInteger(1000);
    private final AtomicInteger tokenSequence = new AtomicInteger(10);

    private OrderService() {
        this.dataStore = DataStore.getInstance();
        this.cartService = CartService.getInstance();
        initSequence();
    }

    private void initSequence() {
        List<Order> existing = dataStore.getOrders();
        if (!existing.isEmpty()) {
            orderSequence.set(1000 + existing.size());
            tokenSequence.set(10 + (existing.size() % 90));
        }
    }

    public static synchronized OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public synchronized Order placeOrder(User student, String sessionToken, String paymentMethod, String notes) {
        if (student == null || sessionToken == null) {
            throw new IllegalArgumentException("Invalid student or session");
        }
        List<OrderItem> cartItems = cartService.getCart(sessionToken);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        // Deep copy cart items for order snapshot
        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;
        for (OrderItem item : cartItems) {
            orderItems.add(new OrderItem(
                    item.getFoodItemId(),
                    item.getFoodName(),
                    item.getUnitPrice(),
                    item.getQuantity(),
                    item.getEmoji()
            ));
            total += item.getSubtotal();
        }

        String orderId = "ord-" + UUID.randomUUID().toString().substring(0, 8);
        String orderNumber = "#ORD-" + orderSequence.incrementAndGet();
        String pickupToken = "TK-" + tokenSequence.incrementAndGet();

        Order order = new Order(
                orderId,
                orderNumber,
                pickupToken,
                student.getId(),
                student.getFullName(),
                student.getRollNumber(),
                student.getPhone(),
                orderItems,
                total,
                paymentMethod,
                notes
        );

        dataStore.addOrder(order);
        cartService.clearCart(sessionToken);
        return order;
    }

    public List<Order> getAllOrders() {
        return dataStore.getOrders();
    }

    public List<Order> getOrdersByUser(String userId) {
        if (userId == null) return Collections.emptyList();
        return dataStore.getOrders().stream()
                .filter(o -> userId.equals(o.getUserId()))
                .collect(Collectors.toList());
    }

    public List<Order> getActiveOrders() {
        return dataStore.getOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.PLACED ||
                             o.getStatus() == OrderStatus.PREPARING ||
                             o.getStatus() == OrderStatus.READY)
                .collect(Collectors.toList());
    }

    public Order getOrderById(String orderId) {
        return dataStore.findOrderById(orderId);
    }

    public synchronized boolean updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = dataStore.findOrderById(orderId);
        if (order != null) {
            order.setStatus(newStatus);
            dataStore.updateOrder(order);
            return true;
        }
        return false;
    }

    // --- Analytics for Canteen Staff ---
    public double getTotalRevenue() {
        return dataStore.getOrders().stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

    public double getTodayRevenue() {
        LocalDate today = LocalDate.now();
        return dataStore.getOrders().stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .filter(o -> o.getOrderTime().toLocalDate().equals(today))
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

    public int getTodayOrderCount() {
        LocalDate today = LocalDate.now();
        return (int) dataStore.getOrders().stream()
                .filter(o -> o.getOrderTime().toLocalDate().equals(today))
                .count();
    }

    public int getActiveOrderCount() {
        return getActiveOrders().size();
    }

    public int getCompletedOrderCount() {
        return (int) dataStore.getOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                .count();
    }

    public Map<String, Integer> getTopSellingItems(int limit) {
        Map<String, Integer> counts = new HashMap<>();
        for (Order o : dataStore.getOrders()) {
            if (o.getStatus() != OrderStatus.CANCELLED) {
                for (OrderItem item : o.getItems()) {
                    counts.put(item.getFoodName(), counts.getOrDefault(item.getFoodName(), 0) + item.getQuantity());
                }
            }
        }
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
