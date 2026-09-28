package com.canteen.storage;

import com.canteen.config.AppConfig;
import com.canteen.model.FoodItem;
import com.canteen.model.Order;
import com.canteen.model.User;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataStore {
    private static DataStore instance;

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, FoodItem> menuItems = new ConcurrentHashMap<>();
    private final List<Order> orders = new CopyOnWriteArrayList<>();

    private DataStore() {
        initStorage();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void initStorage() {
        File dataDir = new File(AppConfig.DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        loadUsers();
        loadMenu();
        loadOrders();

        if (users.isEmpty() || menuItems.isEmpty()) {
            SeedDataLoader.seed(this);
            saveAll();
        }
    }

    public synchronized void saveAll() {
        saveUsers();
        saveMenu();
        saveOrders();
    }

    // --- Users ---
    @SuppressWarnings("unchecked")
    private void loadUsers() {
        File file = new File(AppConfig.USERS_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<User> list = (List<User>) ois.readObject();
            for (User u : list) {
                users.put(u.getId(), u);
            }
        } catch (Exception e) {
            System.err.println("Could not load users file, starting fresh: " + e.getMessage());
        }
    }

    public synchronized void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(AppConfig.USERS_FILE))) {
            oos.writeObject(new ArrayList<>(users.values()));
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
        saveUsers();
    }

    public User findUserByUsername(String username) {
        if (username == null) return null;
        for (User u : users.values()) {
            if (u.getUsername().equalsIgnoreCase(username.trim())) {
                return u;
            }
        }
        return null;
    }

    public User findUserById(String id) {
        return id != null ? users.get(id) : null;
    }

    // --- Menu Items ---
    @SuppressWarnings("unchecked")
    private void loadMenu() {
        File file = new File(AppConfig.MENU_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<FoodItem> list = (List<FoodItem>) ois.readObject();
            for (FoodItem item : list) {
                menuItems.put(item.getId(), item);
            }
        } catch (Exception e) {
            System.err.println("Could not load menu file, starting fresh: " + e.getMessage());
        }
    }

    public synchronized void saveMenu() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(AppConfig.MENU_FILE))) {
            oos.writeObject(new ArrayList<>(menuItems.values()));
        } catch (IOException e) {
            System.err.println("Failed to save menu: " + e.getMessage());
        }
    }

    public Map<String, FoodItem> getMenuItems() {
        return menuItems;
    }

    public void addMenuItem(FoodItem item) {
        menuItems.put(item.getId(), item);
        saveMenu();
    }

    public void updateMenuItem(FoodItem item) {
        menuItems.put(item.getId(), item);
        saveMenu();
    }

    public void deleteMenuItem(String id) {
        menuItems.remove(id);
        saveMenu();
    }

    public FoodItem findMenuItemById(String id) {
        return id != null ? menuItems.get(id) : null;
    }

    // --- Orders ---
    @SuppressWarnings("unchecked")
    private void loadOrders() {
        File file = new File(AppConfig.ORDERS_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Order> list = (List<Order>) ois.readObject();
            orders.clear();
            orders.addAll(list);
        } catch (Exception e) {
            System.err.println("Could not load orders file, starting fresh: " + e.getMessage());
        }
    }

    public synchronized void saveOrders() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(AppConfig.ORDERS_FILE))) {
            oos.writeObject(new ArrayList<>(orders));
        } catch (IOException e) {
            System.err.println("Failed to save orders: " + e.getMessage());
        }
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(0, order); // Add newest at front
        saveOrders();
    }

    public Order findOrderById(String orderId) {
        if (orderId == null) return null;
        for (Order o : orders) {
            if (o.getId().equals(orderId)) {
                return o;
            }
        }
        return null;
    }

    public void updateOrder(Order order) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(order.getId())) {
                orders.set(i, order);
                break;
            }
        }
        saveOrders();
    }
}
