package com.canteen.service;

import com.canteen.model.Category;
import com.canteen.model.FoodItem;
import com.canteen.storage.DataStore;

import java.util.*;
import java.util.stream.Collectors;

public class MenuService {
    private static MenuService instance;
    private final DataStore dataStore;

    private MenuService() {
        this.dataStore = DataStore.getInstance();
    }

    public static synchronized MenuService getInstance() {
        if (instance == null) {
            instance = new MenuService();
        }
        return instance;
    }

    public List<FoodItem> getAllMenuItems() {
        return new ArrayList<>(dataStore.getMenuItems().values());
    }

    public List<FoodItem> getAvailableMenuItems() {
        return dataStore.getMenuItems().values().stream()
                .filter(FoodItem::isAvailable)
                .collect(Collectors.toList());
    }

    public List<FoodItem> filterMenu(Category category, String query, boolean onlyAvailable) {
        String cleanQuery = (query != null) ? query.trim().toLowerCase() : "";
        return dataStore.getMenuItems().values().stream()
                .filter(item -> {
                    if (onlyAvailable && !item.isAvailable()) {
                        return false;
                    }
                    if (category != null && category != Category.ALL && item.getCategory() != category) {
                        return false;
                    }
                    if (!cleanQuery.isEmpty()) {
                        boolean matchName = item.getName().toLowerCase().contains(cleanQuery);
                        boolean matchDesc = item.getDescription().toLowerCase().contains(cleanQuery);
                        return matchName || matchDesc;
                    }
                    return true;
                })
                .sorted(Comparator.comparing(FoodItem::getName))
                .collect(Collectors.toList());
    }

    public FoodItem getItemById(String id) {
        return dataStore.findMenuItemById(id);
    }

    public void addMenuItem(String name, Category category, double price, String description, int prepTimeMinutes, String emoji) {
        String id = "item-" + UUID.randomUUID().toString().substring(0, 8);
        FoodItem item = new FoodItem(id, name, category, price, description, true, prepTimeMinutes, emoji);
        dataStore.addMenuItem(item);
    }

    public void updateMenuItem(String id, String name, Category category, double price, String description, int prepTimeMinutes, String emoji, boolean available) {
        FoodItem item = dataStore.findMenuItemById(id);
        if (item != null) {
            item.setName(name);
            item.setCategory(category);
            item.setPrice(price);
            item.setDescription(description);
            item.setPrepTimeMinutes(prepTimeMinutes);
            if (emoji != null && !emoji.trim().isEmpty()) {
                item.setEmoji(emoji.trim());
            }
            item.setAvailable(available);
            dataStore.updateMenuItem(item);
        }
    }

    public boolean toggleAvailability(String id) {
        FoodItem item = dataStore.findMenuItemById(id);
        if (item != null) {
            item.setAvailable(!item.isAvailable());
            dataStore.updateMenuItem(item);
            return item.isAvailable();
        }
        return false;
    }

    public void deleteMenuItem(String id) {
        dataStore.deleteMenuItem(id);
    }
}
