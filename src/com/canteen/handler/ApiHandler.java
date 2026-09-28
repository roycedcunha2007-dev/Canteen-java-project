package com.canteen.handler;

import com.canteen.model.Order;
import com.canteen.service.OrderService;
import com.canteen.util.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class ApiHandler implements HttpHandler {
    private final OrderService orderService = OrderService.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Map<String, String> queryParams = HttpUtils.parseQueryParams(exchange.getRequestURI().getRawQuery());

        if ("/api/order-status".equals(path)) {
            String orderId = queryParams.get("orderId");
            if (orderId == null) {
                HttpUtils.sendJson(exchange, 400, "{\"error\": \"Missing orderId\"}");
                return;
            }
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                HttpUtils.sendJson(exchange, 404, "{\"error\": \"Order not found\"}");
                return;
            }

            String json = String.format("{\"orderId\": \"%s\", \"orderNumber\": \"%s\", \"pickupToken\": \"%s\", \"status\": \"%s\", \"statusName\": \"%s\", \"step\": %d}",
                    order.getId(),
                    order.getOrderNumber(),
                    order.getPickupToken(),
                    order.getStatus().name(),
                    order.getStatus().getDisplayName(),
                    order.getStatus().getStep()
            );
            HttpUtils.sendJson(exchange, 200, json);
            return;
        }

        if ("/api/stats".equals(path)) {
            String json = String.format("{\"activeOrders\": %d, \"todayOrders\": %d, \"todayRevenue\": %.2f}",
                    orderService.getActiveOrderCount(),
                    orderService.getTodayOrderCount(),
                    orderService.getTodayRevenue()
            );
            HttpUtils.sendJson(exchange, 200, json);
            return;
        }

        HttpUtils.sendJson(exchange, 404, "{\"error\": \"Unknown API endpoint\"}");
    }
}
