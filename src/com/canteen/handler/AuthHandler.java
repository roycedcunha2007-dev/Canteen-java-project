package com.canteen.handler;

import com.canteen.config.AppConfig;
import com.canteen.model.Role;
import com.canteen.model.User;
import com.canteen.service.AuthService;
import com.canteen.service.CartService;
import com.canteen.util.HttpUtils;
import com.canteen.view.AuthView;
import com.canteen.view.LayoutView;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class AuthHandler implements HttpHandler {
    private final AuthService authService = AuthService.getInstance();
    private final CartService cartService = CartService.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if ("/logout".equals(path)) {
            handleLogout(exchange);
            return;
        }

        if ("/login".equals(path)) {
            if ("POST".equalsIgnoreCase(method)) {
                handleLoginPost(exchange);
            } else {
                handleLoginGet(exchange, null, null);
            }
            return;
        }

        if ("/register".equals(path)) {
            if ("POST".equalsIgnoreCase(method)) {
                handleRegisterPost(exchange);
            } else {
                handleRegisterGet(exchange, null);
            }
            return;
        }

        HttpUtils.sendRedirect(exchange, "/login");
    }

    private void handleLoginGet(HttpExchange exchange, String errorMessage, String prefilledUser) throws IOException {
        String sessionToken = HttpUtils.getCookieValue(exchange, AppConfig.SESSION_COOKIE_NAME);
        User current = authService.getUserBySession(sessionToken);
        if (current != null) {
            HttpUtils.sendRedirect(exchange, current.isAdmin() ? "/admin" : "/menu");
            return;
        }

        String content = AuthView.renderLogin(errorMessage, prefilledUser);
        String html = LayoutView.render("Sign In", "login", null, 0, null, null, content);
        HttpUtils.sendHtml(exchange, 200, html);
    }

    private void handleLoginPost(HttpExchange exchange) throws IOException {
        Map<String, String> form = HttpUtils.parseFormData(exchange);
        String username = form.get("username");
        String password = form.get("password");

        String token = authService.login(username, password);
        if (token != null) {
            HttpUtils.setCookie(exchange, AppConfig.SESSION_COOKIE_NAME, token, AppConfig.SESSION_MAX_AGE_SECONDS, "/");
            User user = authService.getUserBySession(token);
            if (user != null && user.isAdmin()) {
                HttpUtils.sendRedirect(exchange, "/admin");
            } else {
                HttpUtils.sendRedirect(exchange, "/menu");
            }
        } else {
            handleLoginGet(exchange, "Invalid username or password. Please try again or use the demo buttons below.", username);
        }
    }

    private void handleRegisterGet(HttpExchange exchange, String errorMessage) throws IOException {
        String sessionToken = HttpUtils.getCookieValue(exchange, AppConfig.SESSION_COOKIE_NAME);
        User current = authService.getUserBySession(sessionToken);
        if (current != null) {
            HttpUtils.sendRedirect(exchange, "/menu");
            return;
        }

        String content = AuthView.renderRegister(errorMessage);
        String html = LayoutView.render("Student Registration", "register", null, 0, null, null, content);
        HttpUtils.sendHtml(exchange, 200, html);
    }

    private void handleRegisterPost(HttpExchange exchange) throws IOException {
        Map<String, String> form = HttpUtils.parseFormData(exchange);
        String username = form.get("username");
        String password = form.get("password");
        String fullName = form.get("fullName");
        String rollNumber = form.get("rollNumber");
        String email = form.get("email");
        String phone = form.get("phone");

        try {
            String token = authService.register(username, password, fullName, rollNumber, email, phone, Role.STUDENT);
            HttpUtils.setCookie(exchange, AppConfig.SESSION_COOKIE_NAME, token, AppConfig.SESSION_MAX_AGE_SECONDS, "/");
            HttpUtils.sendRedirect(exchange, "/menu");
        } catch (IllegalArgumentException e) {
            handleRegisterGet(exchange, e.getMessage());
        }
    }

    private void handleLogout(HttpExchange exchange) throws IOException {
        String sessionToken = HttpUtils.getCookieValue(exchange, AppConfig.SESSION_COOKIE_NAME);
        if (sessionToken != null) {
            authService.logout(sessionToken);
            cartService.clearCart(sessionToken);
        }
        HttpUtils.clearCookie(exchange, AppConfig.SESSION_COOKIE_NAME);
        HttpUtils.sendRedirect(exchange, "/login");
    }
}
