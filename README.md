# CampusBite - College Canteen Management System

An end-to-end, zero-dependency college canteen web application designed to eliminate break-time queues, provide real-time food availability and pricing, and streamline kitchen and billing operations.

**100% Pure Java**: Built strictly and entirely using Java (Java 21 standard library, `com.sun.net.httpserver.HttpServer`). Requires no Maven, Gradle, Node.js, Python, or external databases.

---

## 🚀 How to Run

### Step 1: Compile the Java Code
Open PowerShell or your terminal in this directory:
```powershell
javac -d bin -cp src CanteenApp.java (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

### Step 2: Start the Canteen Web Application
```powershell
java -cp bin CanteenApp
```

### Step 3: Open in Your Browser
Open your browser and navigate to:
👉 **[http://localhost:8080/](http://localhost:8080/)**

---

## 🔑 Pre-Configured Demo Credentials

| Role | Username | Password | Notes |
|---|---|---|---|
| **Student** | `student` | `student123` | Aarav Mehta (Roll: `2024CS101`) |
| **Student 2** | `priya` | `priya123` | Priya Sharma (Roll: `2024EC205`) |
| **Canteen Admin / Staff** | `admin` | `admin123` | Canteen Head Manager |

*(Students can also register brand new accounts directly on the `/register` page with their college roll numbers)*

---

## 🌟 Key Features

### 🎓 Student Portal
1. **Student Registration & Login**: Register with Name, Roll Number, Phone, and Email.
2. **Interactive Menu Catalog**:
   - Filter items by category: *All, Breakfast, Meals & Lunch, Quick Snacks, Beverages & Drinks, Desserts*.
   - Live search bar by item name and description.
   - Real-time stock status badge (`🟢 In Stock` vs `🔴 Sold Out`).
   - Preparation time indicator (e.g. `⏱️ ~8 mins`).
3. **Shopping Cart & Billing**:
   - Add items to cart with dynamic quantity increment/decrement (`+` and `-`).
   - Counter pickup fee calculation (₹0 college student rate).
   - Flexible payment selection: *Pay at Counter (Cash/Card)*, *UPI QR at Counter*, *Campus Smart Card*.
   - Cooking / packaging instructions (e.g. "Less spicy", "Pack in parcel").
4. **Order Tracking & Live Status**:
   - Unique **Pickup Token** (e.g. `TK-14`) prominently displayed to show at the counter.
   - Visual 4-Step Progress Stepper:
     `Order Placed` ➔ `In Kitchen Preparing` ➔ `Ready for Pickup` ➔ `Completed & Picked Up`.
   - Real-time page auto-sync every 7 seconds to immediately notify the student when their meal is ready!

### 👨‍🍳 Canteen Admin / Kitchen Staff Portal
1. **Live Kitchen Order Queue (`/admin/orders`)**:
   - Incoming student orders displayed in real-time with pickup tokens, student details, and preparation notes.
   - 1-Click status transitions:
     - `[👨‍🍳 Start Preparing Food]` (Marks order as `PREPARING`)
     - `[🔔 Mark Ready For Counter Pickup]` (Marks order as `READY`)
     - `[✅ Handed Over / Mark Completed]` (Marks order as `COMPLETED`)
     - `[❌ Cancel Order]`
2. **Menu & Stock Management (`/admin/menu`)**:
   - Add new food items with name, category, price, prep time, and emoji.
   - **Instant 1-Click Stock Toggle**: Switch items between `🟢 In Stock` and `🔴 Sold Out` during rush hour when ingredients run out.
   - Edit pricing and item descriptions or delete items.
3. **Operational Dashboard & Analytics (`/admin`)**:
   - Today's Revenue and Lifetime Sales.
   - Active orders requiring kitchen attention.
   - Total orders processed today.
   - Leaderboard of popular / top-selling food items.

---

## 📂 Project Architecture

```
java aaron/
├── CanteenApp.java                      # Root application entry point
├── src/
│   ├── com/canteen/
│   │   ├── Main.java                    # HTTP Server bootstrap & routing
│   │   ├── config/
│   │   │   └── AppConfig.java           # App constants, port, and paths
│   │   ├── model/                       # Pure Java domain models
│   │   │   ├── Role.java                # STUDENT, ADMIN
│   │   │   ├── Category.java            # Breakfast, Meals, Snacks, Beverages, Desserts
│   │   │   ├── OrderStatus.java         # Placed, Preparing, Ready, Completed, Cancelled
│   │   │   ├── User.java                # User account model
│   │   │   ├── FoodItem.java            # Canteen menu item
│   │   │   ├── OrderItem.java           # Cart / order line item
│   │   │   └── Order.java               # Order representation with token & timestamps
│   │   ├── storage/                     # Zero-dependency persistence
│   │   │   ├── DataStore.java           # Synchronized file storage
│   │   │   └── SeedDataLoader.java      # Pre-seeds 21 realistic college canteen items
│   │   ├── service/                     # Business logic services
│   │   │   ├── AuthService.java         # Authentication & sessions
│   │   │   ├── MenuService.java         # Menu querying & modifications
│   │   │   ├── CartService.java         # Shopping cart state
│   │   │   └── OrderService.java        # Order lifecycle & sales analytics
│   │   ├── util/
│   │   │   └── HttpUtils.java           # Form/query parser, cookies, HTML escape
│   │   ├── view/                        # Pure Java responsive HTML/CSS UI
│   │   │   ├── LayoutView.java          # Modern responsive navigation & stylesheet
│   │   │   ├── AuthView.java            # Login & student registration forms
│   │   │   ├── MenuView.java            # Student catalog, search, and category pills
│   │   │   ├── CartView.java            # Cart review, subtotal calculation, checkout
│   │   │   ├── StudentOrderView.java    # Live progress stepper & pickup tokens
│   │   │   ├── AdminDashboardView.java  # Sales stats & kitchen overview
│   │   │   ├── AdminMenuView.java       # Menu manager & 1-click availability toggles
│   │   │   └── AdminOrderView.java      # Live kitchen queue & status transitions
│   │   └── handler/                     # com.sun.net.httpserver handlers
│   │       ├── AuthHandler.java         # /login, /register, /logout
│   │       ├── MenuHandler.java         # /, /menu
│   │       ├── CartHandler.java         # /cart, /cart/add, /cart/update, /order/place
│   │       ├── StudentOrderHandler.java # /my-orders
│   │       ├── AdminHandler.java        # /admin, /admin/menu, /admin/orders
│   │       └── ApiHandler.java          # /api/order-status, /api/stats
│   └── test/
│       ├── SystemTest.java              # Complete automated service test suite
│       └── HttpIntegrationTest.java     # Network HTTP integration test suite
```

---

## 🧪 Running the Automated Tests

To run the automated pure Java tests:
```powershell
java -ea -cp bin test.SystemTest
java -ea -cp bin test.HttpIntegrationTest
```
