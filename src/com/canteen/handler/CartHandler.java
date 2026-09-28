package com.canteen.handler;

import com.canteen.config.AppConfig;
import com.canteen.model.Order;
import com.canteen.model.OrderItem;
import com.canteen.model.User;
import com.canteen.service.AuthService;
import com.canteen.service.CartService;
import com.canteen.service.OrderService;
import com.canteen.util.HttpUtils;
import com.canteen.view.CartView;
import com.canteen.view.LayoutView;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CartHandler implements HttpHandler {
    private final CartService cartService = CartService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private final OrderService orderService = OrderService.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        String sessionToken = HttpUtils.getCookieValue(exchange, AppConfig.SESSION_COOKIE_NAME);
        if (sessionToken == null) {
            sessionToken = UUID.randomUUID().toString();
            HttpUtils.setCookie(exchange, AppConfig.SESSION_COOKIE_NAME, sessionToken, AppConfig.SESSION_MAX_AGE_SECONDS, "/");
        }

        User currentUser = authService.getUserBySession(sessionToken);

        if ("/cart/add".equals(path) && "POST".equalsIgnoreCase(method)) {
            Map<String, String> form = HttpUtils.parseFormData(exchange);
            String foodItemId = form.get("foodItemId");
            int qty = 1;
            try {
                if (form.containsKey("quantity")) {
                    qty = Integer.parseInt(form.get("quantity"));
                }
            } catch (NumberFormatException ignored) {}

            cartService.addToCart(sessionToken, foodItemId, qty);
            HttpUtils.sendRedirect(exchange, "/menu?added=1");
            return;
        }

        if ("/cart/update".equals(path) && "POST".equalsIgnoreCase(method)) {
            Map<String, String> form = HttpUtils.parseFormData(exchange);
            String foodItemId = form.get("foodItemId");
            int qty = 0;
            try {
                qty = Integer.parseInt(form.get("quantity"));
            } catch (NumberFormatException ignored) {}

            cartService.updateQuantity(sessionToken, foodItemId, qty);
            HttpUtils.sendRedirect(exchange, "/cart");
            return;
        }

        if ("/cart/remove".equals(path) && "POST".equalsIgnoreCase(method)) {
            Map<String, String> form = HttpUtils.parseFormData(exchange);
            String foodItemId = form.get("foodItemId");
            cartService.removeFromCart(sessionToken, foodItemId);
            HttpUtils.sendRedirect(exchange, "/cart");
            return;
        }

        if ("/cart/clear".equals(path) && "POST".equalsIgnoreCase(method)) {
            cartService.clearCart(sessionToken);
            HttpUtils.sendRedirect(exchange, "/cart");
            return;
        }

        if ("/order/place".equals(path) && "POST".equalsIgnoreCase(method)) {
            if (currentUser == null) {
                HttpUtils.sendRedirect(exchange, "/login");
                return;
            }

            Map<String, String> form = HttpUtils.parseFormData(exchange);
            String paymentMethod = form.get("paymentMethod");
            String notes = form.get("notes");

            try {
                Order order = orderService.placeOrder(currentUser, sessionToken, paymentMethod, notes);
                HttpUtils.sendRedirect(exchange, "/my-orders?placed=" + order.getPickupToken());
            } catch (Exception e) {
                HttpUtils.sendRedirect(exchange, "/cart?err=" + HttpUtils.urlEncode(e.getMessage()));
            }
            return;
        }

        // Default: Render Cart Page
        List<OrderItem> cart = cartService.getCart(sessionToken);
        double total = cartService.calculateTotal(sessionToken);
        int cartCount = cartService.getCartItemCount(sessionToken);

        Map<String, String> queryParams = HttpUtils.parseQueryParams(exchange.getRequestURI().getRawQuery());
        String err = queryParams.get("err");

        String content = CartView.render(cart, total, currentUser);
        String html = LayoutView.render("Your Cart", "cart", currentUser, cartCount, err, "error", content);
        HttpUtils.sendHtml(exchange, 200, html);
    }
}
