package com.canteen.handler;

import com.canteen.config.AppConfig;
import com.canteen.model.*;
import com.canteen.service.AuthService;
import com.canteen.service.MenuService;
import com.canteen.service.OrderService;
import com.canteen.util.HttpUtils;
import com.canteen.view.AdminDashboardView;
import com.canteen.view.AdminMenuView;
import com.canteen.view.AdminOrderView;
import com.canteen.view.LayoutView;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminHandler implements HttpHandler {
    private final AuthService authService = AuthService.getInstance();
    private final MenuService menuService = MenuService.getInstance();
    private final OrderService orderService = OrderService.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String sessionToken = HttpUtils.getCookieValue(exchange, AppConfig.SESSION_COOKIE_NAME);
        User currentUser = authService.getUserBySession(sessionToken);

        if (currentUser == null || !currentUser.isAdmin()) {
            HttpUtils.sendRedirect(exchange, "/login");
            return;
        }

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        // 1. Live Kitchen Orders Status Update (POST)
        if ("/admin/orders/status".equals(path) && "POST".equalsIgnoreCase(method)) {
            Map<String, String> form = HttpUtils.parseFormData(exchange);
            String orderId = form.get("orderId");
            String newStatusStr = form.get("newStatus");
            if (orderId != null && newStatusStr != null) {
                OrderStatus status = OrderStatus.fromString(newStatusStr);
                orderService.updateOrderStatus(orderId, status);
            }
            HttpUtils.sendRedirect(exchange, "/admin/orders");
            return;
        }

        // 2. Kitchen Live Orders View (GET)
        if ("/admin/orders".equals(path)) {
            Map<String, String> qp = HttpUtils.parseQueryParams(exchange.getRequestURI().getRawQuery());
            String filter = qp.get("filter");
            if (filter == null || filter.trim().isEmpty()) {
                filter = "active";
            }

            List<Order> filtered;
            switch (filter.toLowerCase()) {
                case "all":
                    filtered = orderService.getAllOrders();
                    break;
                case "placed":
                    filtered = orderService.getAllOrders().stream()
                            .filter(o -> o.getStatus() == OrderStatus.PLACED)
                            .collect(Collectors.toList());
                    break;
                case "preparing":
                    filtered = orderService.getAllOrders().stream()
                            .filter(o -> o.getStatus() == OrderStatus.PREPARING)
                            .collect(Collectors.toList());
                    break;
                case "ready":
                    filtered = orderService.getAllOrders().stream()
                            .filter(o -> o.getStatus() == OrderStatus.READY)
                            .collect(Collectors.toList());
                    break;
                case "completed":
                    filtered = orderService.getAllOrders().stream()
                            .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                            .collect(Collectors.toList());
                    break;
                case "active":
                default:
                    filtered = orderService.getActiveOrders();
                    break;
            }

            String content = AdminOrderView.render(filtered, filter);
            String html = LayoutView.render("Kitchen Orders Queue", "admin-orders", currentUser, 0, null, null, content);
            HttpUtils.sendHtml(exchange, 200, html);
            return;
        }

        // 3. Menu Management: Add Item (POST)
        if ("/admin/menu/add".equals(path) && "POST".equalsIgnoreCase(method)) {
            Map<String, String> form = HttpUtils.parseFormData(exchange);
            String name = form.get("name");
            String catStr = form.get("category");
            String priceStr = form.get("price");
            String desc = form.get("description");
            String prepStr = form.get("prepTimeMinutes");
            String emoji = form.get("emoji");

            double price = 0.0;
            try { price = Double.parseDouble(priceStr); } catch (Exception ignored) {}
            int prep = 10;
            try { prep = Integer.parseInt(prepStr); } catch (Exception ignored) {}

            menuService.addMenuItem(name, Category.fromString(catStr), price, desc, prep, emoji);
            HttpUtils.sendRedirect(exchange, "/admin/menu?msg=Item+added+successfully");
            return;
        }

        // 4. Menu Management: Update Item (POST)
        if ("/admin/menu/update".equals(path) && "POST".equalsIgnoreCase(method)) {
            Map<String, String> form = HttpUtils.parseFormData(exchange);
            String id = form.get("id");
            String name = form.get("name");
            String catStr = form.get("category");
            String priceStr = form.get("price");
            String desc = form.get("description");
            String prepStr = form.get("prepTimeMinutes");
            String emoji = form.get("emoji");

            FoodItem existing = menuService.getItemById(id);
            boolean available = existing != null && existing.isAvailable();

            double price = existing != null ? existing.getPrice() : 0.0;
            try { price = Double.parseDouble(priceStr); } catch (Exception ignored) {}
            int prep = existing != null ? existing.getPrepTimeMinutes() : 8;
            try { prep = Integer.parseInt(prepStr); } catch (Exception ignored) {}

            menuService.updateMenuItem(id, name, Category.fromString(catStr), price, desc, prep, emoji, available);
            HttpUtils.sendRedirect(exchange, "/admin/menu?msg=Item+updated+successfully");
            return;
        }

        // 5. Menu Management: Toggle Stock Availability (POST)
        if ("/admin/menu/toggle".equals(path) && "POST".equalsIgnoreCase(method)) {
            Map<String, String> form = HttpUtils.parseFormData(exchange);
            String id = form.get("id");
            boolean nowAvail = menuService.toggleAvailability(id);
            HttpUtils.sendRedirect(exchange, "/admin/menu?msg=Stock+status+toggled+to+" + (nowAvail ? "In+Stock" : "Sold+Out"));
            return;
        }

        // 6. Menu Management: Delete Item (POST)
        if ("/admin/menu/delete".equals(path) && "POST".equalsIgnoreCase(method)) {
            Map<String, String> form = HttpUtils.parseFormData(exchange);
            String id = form.get("id");
            menuService.deleteMenuItem(id);
            HttpUtils.sendRedirect(exchange, "/admin/menu?msg=Item+deleted");
            return;
        }

        // 7. Menu Management View (GET)
        if ("/admin/menu".equals(path)) {
            Map<String, String> qp = HttpUtils.parseQueryParams(exchange.getRequestURI().getRawQuery());
            String editId = qp.get("edit");
            String msg = qp.get("msg");
            FoodItem editItem = editId != null ? menuService.getItemById(editId) : null;
            List<FoodItem> items = menuService.getAllMenuItems();

            String content = AdminMenuView.render(items, editItem);
            String html = LayoutView.render("Manage Menu", "admin-menu", currentUser, 0, msg, "success", content);
            HttpUtils.sendHtml(exchange, 200, html);
            return;
        }

        // 8. Admin Dashboard (Default /admin)
        double totalRev = orderService.getTotalRevenue();
        double todayRev = orderService.getTodayRevenue();
        int todayOrders = orderService.getTodayOrderCount();
        int activeOrders = orderService.getActiveOrderCount();
        int completedOrders = orderService.getCompletedOrderCount();
        Map<String, Integer> topSelling = orderService.getTopSellingItems(5);
        List<Order> recentOrders = orderService.getAllOrders();

        String content = AdminDashboardView.render(totalRev, todayRev, todayOrders, activeOrders, completedOrders, topSelling, recentOrders);
        String html = LayoutView.render("Admin Dashboard", "dashboard", currentUser, 0, null, null, content);
        HttpUtils.sendHtml(exchange, 200, html);
    }
}
