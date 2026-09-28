package com.canteen;

import com.canteen.config.AppConfig;
import com.canteen.handler.*;
import com.canteen.storage.DataStore;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        int port = AppConfig.DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        // Initialize datastore and seed data
        DataStore.getInstance();

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            // Register Handlers
            AuthHandler authHandler = new AuthHandler();
            MenuHandler menuHandler = new MenuHandler();
            CartHandler cartHandler = new CartHandler();
            StudentOrderHandler studentOrderHandler = new StudentOrderHandler();
            AdminHandler adminHandler = new AdminHandler();
            ApiHandler apiHandler = new ApiHandler();

            server.createContext("/", menuHandler);
            server.createContext("/menu", menuHandler);
            server.createContext("/login", authHandler);
            server.createContext("/register", authHandler);
            server.createContext("/logout", authHandler);

            server.createContext("/cart", cartHandler);
            server.createContext("/cart/add", cartHandler);
            server.createContext("/cart/update", cartHandler);
            server.createContext("/cart/remove", cartHandler);
            server.createContext("/cart/clear", cartHandler);
            server.createContext("/order/place", cartHandler);

            server.createContext("/my-orders", studentOrderHandler);

            server.createContext("/admin", adminHandler);
            server.createContext("/admin/menu", adminHandler);
            server.createContext("/admin/menu/add", adminHandler);
            server.createContext("/admin/menu/update", adminHandler);
            server.createContext("/admin/menu/toggle", adminHandler);
            server.createContext("/admin/menu/delete", adminHandler);
            server.createContext("/admin/orders", adminHandler);
            server.createContext("/admin/orders/status", adminHandler);

            server.createContext("/api/order-status", apiHandler);
            server.createContext("/api/stats", apiHandler);

            // Java 21 Virtual Threads executor for lightweight, ultra-high concurrency
            server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

            server.start();

            printStartupBanner(port);

            // Add clean shutdown hook
            final int serverPort = port;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nStopping CampusBite Canteen Server...");
                DataStore.getInstance().saveAll();
                server.stop(1);
                System.out.println("Canteen Server stopped cleanly.");
            }));

        } catch (IOException e) {
            System.err.println("Failed to start server on port " + port + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printStartupBanner(int port) {
        System.out.println("==================================================================");
        System.out.println("   🍔  " + AppConfig.APP_NAME.toUpperCase() + "  🍔   ");
        System.out.println("   100% Pure Java College Dining & Kitchen Management System      ");
        System.out.println("==================================================================");
        System.out.println(" Server successfully running at: http://localhost:" + port + "/");
        System.out.println(" ----------------------------------------------------------------");
        System.out.println(" Demo Student Account:  Username: student  | Password: student123");
        System.out.println(" Demo Admin Account:    Username: admin    | Password: admin123");
        System.out.println(" ----------------------------------------------------------------");
        System.out.println(" Key Routes:");
        System.out.println("   - Student Menu:         http://localhost:" + port + "/menu");
        System.out.println("   - Student Cart:         http://localhost:" + port + "/cart");
        System.out.println("   - Student Orders:       http://localhost:" + port + "/my-orders");
        System.out.println("   - Admin Dashboard:      http://localhost:" + port + "/admin");
        System.out.println("   - Kitchen Order Queue:  http://localhost:" + port + "/admin/orders");
        System.out.println("   - Menu Item Manager:    http://localhost:" + port + "/admin/menu");
        System.out.println("==================================================================");
    }
}
