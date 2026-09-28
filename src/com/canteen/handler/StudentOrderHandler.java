package com.canteen.handler;

import com.canteen.config.AppConfig;
import com.canteen.model.Order;
import com.canteen.model.User;
import com.canteen.service.AuthService;
import com.canteen.service.CartService;
import com.canteen.service.OrderService;
import com.canteen.util.HttpUtils;
import com.canteen.view.LayoutView;
import com.canteen.view.StudentOrderView;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StudentOrderHandler implements HttpHandler {
    private final AuthService authService = AuthService.getInstance();
    private final OrderService orderService = OrderService.getInstance();
    private final CartService cartService = CartService.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String sessionToken = HttpUtils.getCookieValue(exchange, AppConfig.SESSION_COOKIE_NAME);
        User currentUser = authService.getUserBySession(sessionToken);

        if (currentUser == null) {
            HttpUtils.sendRedirect(exchange, "/login");
            return;
        }

        Map<String, String> queryParams = HttpUtils.parseQueryParams(exchange.getRequestURI().getRawQuery());
        String placedToken = queryParams.get("placed");

        String alertMessage = null;
        String alertType = null;
        if (placedToken != null && !placedToken.isEmpty()) {
            alertMessage = "🎉 Order placed successfully! Your Counter Pickup Token is [" + placedToken + "]. Keep this handy!";
            alertType = "success";
        }

        List<Order> orders = orderService.getOrdersByUser(currentUser.getId());
        int cartCount = cartService.getCartItemCount(sessionToken);

        String content = StudentOrderView.render(orders);
        String html = LayoutView.render("My Orders", "orders", currentUser, cartCount, alertMessage, alertType, content);
        HttpUtils.sendHtml(exchange, 200, html);
    }
}
