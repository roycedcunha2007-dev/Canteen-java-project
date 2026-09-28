package com.canteen.handler;

import com.canteen.config.AppConfig;
import com.canteen.model.Category;
import com.canteen.model.FoodItem;
import com.canteen.model.User;
import com.canteen.service.AuthService;
import com.canteen.service.CartService;
import com.canteen.service.MenuService;
import com.canteen.util.HttpUtils;
import com.canteen.view.LayoutView;
import com.canteen.view.MenuView;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MenuHandler implements HttpHandler {
    private final MenuService menuService = MenuService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private final CartService cartService = CartService.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String sessionToken = HttpUtils.getCookieValue(exchange, AppConfig.SESSION_COOKIE_NAME);
        User currentUser = authService.getUserBySession(sessionToken);

        Map<String, String> queryParams = HttpUtils.parseQueryParams(exchange.getRequestURI().getRawQuery());
        String categoryStr = queryParams.get("category");
        String searchQuery = queryParams.get("q");
        String added = queryParams.get("added");

        Category selectedCategory = Category.fromString(categoryStr);
        List<FoodItem> items = menuService.filterMenu(selectedCategory, searchQuery, false);

        int cartCount = sessionToken != null ? cartService.getCartItemCount(sessionToken) : 0;
        double cartTotal = sessionToken != null ? cartService.calculateTotal(sessionToken) : 0.0;

        String alertMessage = null;
        String alertType = null;
        if ("1".equals(added)) {
            alertMessage = "Item successfully added to your cart!";
            alertType = "success";
        }

        String content = MenuView.render(items, selectedCategory, searchQuery, currentUser, cartCount, cartTotal);
        String html = LayoutView.render("Menu", "menu", currentUser, cartCount, alertMessage, alertType, content);
        HttpUtils.sendHtml(exchange, 200, html);
    }
}
