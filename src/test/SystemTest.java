package test;

import com.canteen.model.*;
import com.canteen.service.AuthService;
import com.canteen.service.CartService;
import com.canteen.service.MenuService;
import com.canteen.service.OrderService;
import com.canteen.storage.DataStore;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SystemTest {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  RUNNING PURE JAVA CANTEEN MANAGEMENT SYSTEM TESTS");
        System.out.println("=================================================");

        int passed = 0;
        int failed = 0;

        // Initialize Store
        DataStore store = DataStore.getInstance();
        AuthService authService = AuthService.getInstance();
        MenuService menuService = MenuService.getInstance();
        CartService cartService = CartService.getInstance();
        OrderService orderService = OrderService.getInstance();

        // TEST 1: Auth & Login
        try {
            System.out.print("[TEST 1] Student and Admin Authentication... ");
            String adminToken = authService.login("admin", "admin123");
            assert adminToken != null : "Admin login failed";
            User admin = authService.getUserBySession(adminToken);
            assert admin != null && admin.isAdmin() : "Admin role mismatch";

            String studentToken = authService.login("student", "student123");
            assert studentToken != null : "Student login failed";
            User student = authService.getUserBySession(studentToken);
            assert student != null && !student.isAdmin() : "Student role mismatch";

            // Test Registration
            String testUser = "testuser_" + System.currentTimeMillis();
            String regToken = authService.register(testUser, "pass123", "Test Student", "2024TEST01", "test@college.edu", "9999999999", Role.STUDENT);
            assert regToken != null : "Registration token null";
            User registered = authService.getUserBySession(regToken);
            assert registered != null && registered.getUsername().equals(testUser) : "Registered user mismatch";

            System.out.println("PASSED ✅");
            passed++;
        } catch (Throwable t) {
            System.out.println("FAILED ❌: " + t.getMessage());
            failed++;
        }

        // TEST 2: Menu Filtering & Search
        try {
            System.out.print("[TEST 2] Menu catalog, search, and category filters... ");
            List<FoodItem> allItems = menuService.getAllMenuItems();
            assert allItems.size() >= 20 : "Expected at least 20 seeded items, got " + allItems.size();

            List<FoodItem> breakfastItems = menuService.filterMenu(Category.BREAKFAST, null, false);
            assert !breakfastItems.isEmpty() : "No breakfast items found";
            for (FoodItem item : breakfastItems) {
                assert item.getCategory() == Category.BREAKFAST : "Item is not breakfast: " + item.getName();
            }

            List<FoodItem> dosaMatches = menuService.filterMenu(null, "Dosa", false);
            assert !dosaMatches.isEmpty() : "Search for 'Dosa' returned nothing";
            assert dosaMatches.get(0).getName().toLowerCase().contains("dosa") : "Name mismatch in search";

            System.out.println("PASSED ✅");
            passed++;
        } catch (Throwable t) {
            System.out.println("FAILED ❌: " + t.getMessage());
            failed++;
        }

        // TEST 3: Menu Item Management & Stock Availability Toggle
        try {
            System.out.print("[TEST 3] Admin menu management and stock toggling... ");
            int initialCount = menuService.getAllMenuItems().size();
            menuService.addMenuItem("Special Masala Bun", Category.SNACKS, 45.0, "Fresh baked bun with spicy aloo filling", 5, "🍔");
            assert menuService.getAllMenuItems().size() == initialCount + 1 : "Item addition failed";

            FoodItem added = menuService.filterMenu(null, "Special Masala Bun", false).get(0);
            assert added.isAvailable() : "New item should be available";

            // 1-Click Toggle Availability
            boolean nowAvail = menuService.toggleAvailability(added.getId());
            assert !nowAvail : "Expected item to become Out of Stock";
            assert !menuService.getItemById(added.getId()).isAvailable() : "Item state not persisted";

            // Toggle back
            nowAvail = menuService.toggleAvailability(added.getId());
            assert nowAvail : "Expected item to become In Stock";

            System.out.println("PASSED ✅");
            passed++;
        } catch (Throwable t) {
            System.out.println("FAILED ❌: " + t.getMessage());
            failed++;
        }

        // TEST 4: Shopping Cart Operations
        try {
            System.out.print("[TEST 4] Cart addition, quantity updates, and bill calculation... ");
            String testSession = "test-session-" + UUID.randomUUID();
            FoodItem dosa = menuService.filterMenu(null, "Crispy Masala Dosa", false).get(0);
            FoodItem chai = menuService.filterMenu(null, "Kadak Masala Chai", false).get(0);

            cartService.addToCart(testSession, dosa.getId(), 2);
            cartService.addToCart(testSession, chai.getId(), 3);

            assert cartService.getCartItemCount(testSession) == 5 : "Cart item count mismatch: " + cartService.getCartItemCount(testSession);
            double expectedTotal = (dosa.getPrice() * 2) + (chai.getPrice() * 3);
            double actualTotal = cartService.calculateTotal(testSession);
            assert Math.abs(expectedTotal - actualTotal) < 0.01 : "Total mismatch: expected " + expectedTotal + ", got " + actualTotal;

            // Update quantity of chai from 3 to 1
            cartService.updateQuantity(testSession, chai.getId(), 1);
            assert cartService.getCartItemCount(testSession) == 3 : "Updated cart count mismatch";

            System.out.println("PASSED ✅");
            passed++;
        } catch (Throwable t) {
            System.out.println("FAILED ❌: " + t.getMessage());
            failed++;
        }

        // TEST 5: Order Placement, Status Stepper & Metrics
        try {
            System.out.print("[TEST 5] Order placement, pickup token, and status lifecycle... ");
            String studentToken = authService.login("student", "student123");
            User student = authService.getUserBySession(studentToken);

            // Populate student cart
            cartService.clearCart(studentToken);
            FoodItem biryani = menuService.filterMenu(null, "Biryani", false).get(0);
            cartService.addToCart(studentToken, biryani.getId(), 1);

            Order order = orderService.placeOrder(student, studentToken, "Pay at Canteen Counter", "Extra raita please");
            assert order != null : "Order placement failed";
            assert order.getPickupToken() != null && order.getPickupToken().startsWith("TK-") : "Invalid pickup token: " + order.getPickupToken();
            assert order.getOrderNumber() != null && order.getOrderNumber().startsWith("#ORD-") : "Invalid order number";
            assert order.getStatus() == OrderStatus.PLACED : "Initial status should be PLACED";
            assert cartService.getCartItemCount(studentToken) == 0 : "Cart was not cleared after order placement";

            // Status transitions by staff
            boolean step1 = orderService.updateOrderStatus(order.getId(), OrderStatus.PREPARING);
            assert step1 && order.getStatus() == OrderStatus.PREPARING : "Transition to PREPARING failed";

            boolean step2 = orderService.updateOrderStatus(order.getId(), OrderStatus.READY);
            assert step2 && order.getStatus() == OrderStatus.READY : "Transition to READY failed";

            boolean step3 = orderService.updateOrderStatus(order.getId(), OrderStatus.COMPLETED);
            assert step3 && order.getStatus() == OrderStatus.COMPLETED : "Transition to COMPLETED failed";

            // Metrics
            assert orderService.getTotalRevenue() > 0 : "Total revenue should be > 0";
            Map<String, Integer> topSelling = orderService.getTopSellingItems(5);
            assert !topSelling.isEmpty() : "Top selling items should not be empty";

            System.out.println("PASSED ✅");
            passed++;
        } catch (Throwable t) {
            System.out.println("FAILED ❌: " + t.getMessage());
            failed++;
        }

        System.out.println("=================================================");
        System.out.println("TEST SUMMARY: " + passed + " Passed, " + failed + " Failed");
        System.out.println("=================================================");

        if (failed > 0) {
            System.exit(1);
        }
    }
}
