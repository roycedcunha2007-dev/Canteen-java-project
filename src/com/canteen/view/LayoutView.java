package com.canteen.view;

import com.canteen.config.AppConfig;
import com.canteen.model.Role;
import com.canteen.model.User;

public class LayoutView {

    public static String render(String title, String activeNav, User currentUser, int cartCount, String alertMessage, String alertType, String content) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n");
        sb.append("<meta charset=\"UTF-8\">\n");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=5.0, viewport-fit=cover\">\n");
        sb.append("<meta name=\"theme-color\" content=\"#ea580c\">\n");
        sb.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n");
        sb.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"default\">\n");
        sb.append("<title>").append(escape(title)).append(" - ").append(AppConfig.APP_NAME).append("</title>\n");
        sb.append("<style>\n").append(getCssStyles()).append("\n</style>\n");
        sb.append("</head>\n<body>\n");

        // Top Navigation Bar
        sb.append("<header class=\"navbar\">\n");
        sb.append("  <div class=\"nav-container\">\n");
        sb.append("    <a href=\"/\" class=\"brand\">\n");
        sb.append("      <span class=\"brand-icon\">🍔</span>\n");
        sb.append("      <div class=\"brand-text\">\n");
        sb.append("        <span class=\"brand-title\">CampusBite</span>\n");
        sb.append("        <span class=\"brand-sub\">College Canteen</span>\n");
        sb.append("      </div>\n");
        sb.append("    </a>\n");

        // Desktop Links (hidden on mobile, moved to bottom nav)
        sb.append("    <nav class=\"nav-links desktop-only\">\n");
        sb.append("      <a href=\"/menu\" class=\"nav-link ").append("menu".equals(activeNav) ? "active" : "").append("\">🍽️ Menu</a>\n");

        if (currentUser != null && currentUser.getRole() == Role.STUDENT) {
            sb.append("      <a href=\"/cart\" class=\"nav-link cart-link ").append("cart".equals(activeNav) ? "active" : "").append("\">\n");
            sb.append("        🛒 Cart\n");
            if (cartCount > 0) {
                sb.append("        <span class=\"cart-badge\">").append(cartCount).append("</span>\n");
            }
            sb.append("      </a>\n");
            sb.append("      <a href=\"/my-orders\" class=\"nav-link ").append("orders".equals(activeNav) ? "active" : "").append("\">📦 My Orders</a>\n");
        }

        if (currentUser != null && currentUser.isAdmin()) {
            sb.append("      <a href=\"/admin\" class=\"nav-link ").append("dashboard".equals(activeNav) ? "active" : "").append("\">📊 Dashboard</a>\n");
            sb.append("      <a href=\"/admin/orders\" class=\"nav-link ").append("admin-orders".equals(activeNav) ? "active" : "").append("\">🔔 Kitchen Orders</a>\n");
            sb.append("      <a href=\"/admin/menu\" class=\"nav-link ").append("admin-menu".equals(activeNav) ? "active" : "").append("\">📋 Manage Menu</a>\n");
        }
        sb.append("    </nav>\n");

        // Auth section on right
        sb.append("    <div class=\"nav-auth\">\n");
        if (currentUser != null) {
            sb.append("      <div class=\"user-badge\">\n");
            sb.append("        <span class=\"user-avatar\">").append(currentUser.isAdmin() ? "👨‍🍳" : "🎓").append("</span>\n");
            sb.append("        <div class=\"user-info\">\n");
            sb.append("          <span class=\"user-name\">").append(escape(currentUser.getFullName())).append("</span>\n");
            sb.append("          <span class=\"user-role\">").append(currentUser.isAdmin() ? "Staff" : (currentUser.getRollNumber() != null ? currentUser.getRollNumber() : "Student")).append("</span>\n");
            sb.append("        </div>\n");
            sb.append("      </div>\n");
            sb.append("      <a href=\"/logout\" class=\"btn btn-outline btn-sm desktop-only\">Sign Out</a>\n");
        } else {
            sb.append("      <a href=\"/login\" class=\"btn btn-primary btn-sm\">Sign In</a>\n");
        }
        sb.append("    </div>\n");
        sb.append("  </div>\n");
        sb.append("</header>\n");

        // Flash Alert if present
        if (alertMessage != null && !alertMessage.trim().isEmpty()) {
            String alertClass = "alert-" + (alertType != null ? alertType : "info");
            sb.append("<div class=\"container alert-container\">\n");
            sb.append("  <div class=\"alert ").append(alertClass).append("\">\n");
            sb.append("    <span>").append(escape(alertMessage)).append("</span>\n");
            sb.append("  </div>\n");
            sb.append("</div>\n");
        }

        // Main Content Area
        sb.append("<main class=\"main-content\">\n");
        sb.append("  <div class=\"container\">\n");
        sb.append(content);
        sb.append("  </div>\n");
        sb.append("</main>\n");

        // Mobile Fixed Bottom Navigation Bar (Food App Style for Phones)
        sb.append("<nav class=\"mobile-bottom-nav\">\n");

        // Menu Tab
        sb.append("  <a href=\"/menu\" class=\"mobile-nav-item ").append("menu".equals(activeNav) ? "active" : "").append("\">\n");
        sb.append("    <span class=\"mobile-nav-icon\">🍽️</span>\n");
        sb.append("    <span class=\"mobile-nav-label\">Menu</span>\n");
        sb.append("  </a>\n");

        // Cart Tab (with badge)
        sb.append("  <a href=\"/cart\" class=\"mobile-nav-item ").append("cart".equals(activeNav) ? "active" : "").append("\">\n");
        sb.append("    <span class=\"mobile-nav-icon\" style=\"position: relative;\">\n");
        sb.append("      🛒\n");
        if (cartCount > 0) {
            sb.append("      <span class=\"mobile-cart-badge\">").append(cartCount).append("</span>\n");
        }
        sb.append("    </span>\n");
        sb.append("    <span class=\"mobile-nav-label\">Cart</span>\n");
        sb.append("  </a>\n");

        if (currentUser != null && currentUser.isAdmin()) {
            // Admin Tabs
            sb.append("  <a href=\"/admin/orders\" class=\"mobile-nav-item ").append("admin-orders".equals(activeNav) ? "active" : "").append("\">\n");
            sb.append("    <span class=\"mobile-nav-icon\">🔔</span>\n");
            sb.append("    <span class=\"mobile-nav-label\">Kitchen</span>\n");
            sb.append("  </a>\n");

            sb.append("  <a href=\"/admin/menu\" class=\"mobile-nav-item ").append("admin-menu".equals(activeNav) ? "active" : "").append("\">\n");
            sb.append("    <span class=\"mobile-nav-icon\">📋</span>\n");
            sb.append("    <span class=\"mobile-nav-label\">Stock</span>\n");
            sb.append("  </a>\n");

            sb.append("  <a href=\"/admin\" class=\"mobile-nav-item ").append("dashboard".equals(activeNav) ? "active" : "").append("\">\n");
            sb.append("    <span class=\"mobile-nav-icon\">📊</span>\n");
            sb.append("    <span class=\"mobile-nav-label\">Stats</span>\n");
            sb.append("  </a>\n");
        } else {
            // Student Tabs
            sb.append("  <a href=\"/my-orders\" class=\"mobile-nav-item ").append("orders".equals(activeNav) ? "active" : "").append("\">\n");
            sb.append("    <span class=\"mobile-nav-icon\">📦</span>\n");
            sb.append("    <span class=\"mobile-nav-label\">My Orders</span>\n");
            sb.append("  </a>\n");

            if (currentUser != null) {
                sb.append("  <a href=\"/logout\" class=\"mobile-nav-item\" onclick=\"return confirm('Sign out of your account?');\">\n");
                sb.append("    <span class=\"mobile-nav-icon\">🚪</span>\n");
                sb.append("    <span class=\"mobile-nav-label\">Logout</span>\n");
                sb.append("  </a>\n");
            } else {
                sb.append("  <a href=\"/login\" class=\"mobile-nav-item ").append("login".equals(activeNav) ? "active" : "").append("\">\n");
                sb.append("    <span class=\"mobile-nav-icon\">👤</span>\n");
                sb.append("    <span class=\"mobile-nav-label\">Sign In</span>\n");
                sb.append("  </a>\n");
            }
        }

        sb.append("</nav>\n");

        // Footer (desktop only or soft on mobile)
        sb.append("<footer class=\"footer desktop-only\">\n");
        sb.append("  <div class=\"container footer-content\">\n");
        sb.append("    <p>🎓 <strong>CampusBite Canteen System</strong> &bull; Fast, Queueless College Dining &bull; Built 100% in Java</p>\n");
        sb.append("    <p class=\"text-muted\">Timings: Monday - Saturday (8:00 AM - 6:30 PM) &bull; Fresh & Hygienic</p>\n");
        sb.append("  </div>\n");
        sb.append("</footer>\n");

        sb.append("</body>\n</html>\n");
        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    private static String getCssStyles() {
        return """
            :root {
                --primary: #ea580c;
                --primary-hover: #c2410c;
                --primary-light: #ffedd5;
                --secondary: #0f172a;
                --accent: #10b981;
                --bg-base: #f8fafc;
                --card-bg: #ffffff;
                --text-main: #1e293b;
                --text-muted: #64748b;
                --border-color: #e2e8f0;
                --shadow-sm: 0 1px 2px 0 rgb(0 0 0 / 0.05);
                --shadow-md: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
                --shadow-lg: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
                --radius: 14px;
            }

            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
                -webkit-tap-highlight-color: transparent;
            }

            body {
                font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
                background-color: var(--bg-base);
                color: var(--text-main);
                line-height: 1.5;
                min-height: 100vh;
                display: flex;
                flex-direction: column;
                touch-action: manipulation;
            }

            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 20px;
                width: 100%;
            }

            /* Navbar */
            .navbar {
                background: #ffffff;
                border-bottom: 1px solid var(--border-color);
                position: sticky;
                top: 0;
                z-index: 100;
                box-shadow: var(--shadow-sm);
            }
            .nav-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 10px 20px;
                display: flex;
                align-items: center;
                justify-content: space-between;
                gap: 16px;
            }
            .brand {
                display: flex;
                align-items: center;
                gap: 10px;
                text-decoration: none;
                color: inherit;
            }
            .brand-icon {
                font-size: 1.8rem;
                background: var(--primary-light);
                width: 40px;
                height: 40px;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 10px;
            }
            .brand-title {
                font-size: 1.2rem;
                font-weight: 700;
                color: var(--secondary);
                display: block;
                line-height: 1.1;
            }
            .brand-sub {
                font-size: 0.7rem;
                color: var(--primary);
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }
            .nav-links {
                display: flex;
                align-items: center;
                gap: 8px;
            }
            .nav-link {
                text-decoration: none;
                color: var(--text-muted);
                padding: 8px 14px;
                border-radius: 8px;
                font-weight: 500;
                font-size: 0.95rem;
                transition: all 0.2s;
                display: flex;
                align-items: center;
                gap: 6px;
            }
            .nav-link:hover, .nav-link.active {
                color: var(--primary);
                background-color: var(--primary-light);
            }
            .cart-link {
                position: relative;
            }
            .cart-badge {
                background-color: var(--primary);
                color: #ffffff;
                font-size: 0.75rem;
                font-weight: 700;
                padding: 2px 7px;
                border-radius: 20px;
            }

            .nav-auth {
                display: flex;
                align-items: center;
                gap: 10px;
            }
            .user-badge {
                display: flex;
                align-items: center;
                gap: 8px;
                background: #f1f5f9;
                padding: 5px 10px;
                border-radius: 20px;
            }
            .user-avatar {
                font-size: 1.2rem;
            }
            .user-info {
                display: flex;
                flex-direction: column;
                text-align: left;
            }
            .user-name {
                font-size: 0.85rem;
                font-weight: 600;
                line-height: 1.1;
                color: var(--secondary);
            }
            .user-role {
                font-size: 0.7rem;
                color: var(--text-muted);
            }

            /* Buttons (touch-optimized) */
            .btn {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                gap: 6px;
                font-weight: 600;
                font-size: 0.95rem;
                min-height: 42px;
                padding: 8px 18px;
                border-radius: 10px;
                border: 1px solid transparent;
                cursor: pointer;
                text-decoration: none;
                transition: all 0.15s ease-in-out;
                user-select: none;
            }
            .btn:active {
                transform: scale(0.98);
            }
            .btn-primary {
                background-color: var(--primary);
                color: #ffffff;
            }
            .btn-primary:hover {
                background-color: var(--primary-hover);
            }
            .btn-secondary {
                background-color: var(--secondary);
                color: #ffffff;
            }
            .btn-secondary:hover {
                background-color: #1e293b;
            }
            .btn-success {
                background-color: var(--accent);
                color: #ffffff;
            }
            .btn-success:hover {
                background-color: #059669;
            }
            .btn-danger {
                background-color: #ef4444;
                color: #ffffff;
            }
            .btn-danger:hover {
                background-color: #dc2626;
            }
            .btn-outline {
                background-color: transparent;
                border-color: var(--border-color);
                color: var(--text-main);
            }
            .btn-outline:hover {
                background-color: #f1f5f9;
            }
            .btn-sm {
                min-height: 34px;
                padding: 5px 12px;
                font-size: 0.85rem;
            }
            .btn-block {
                width: 100%;
            }
            .btn:disabled {
                opacity: 0.5;
                cursor: not-allowed;
            }

            /* Content Area */
            .main-content {
                flex: 1;
                padding: 24px 0 40px;
            }

            /* Alerts */
            .alert-container {
                margin-top: 12px;
            }
            .alert {
                padding: 12px 16px;
                border-radius: 10px;
                font-size: 0.95rem;
                font-weight: 500;
                display: flex;
                align-items: center;
                justify-content: space-between;
            }
            .alert-success {
                background-color: #d1fae5;
                color: #065f46;
                border: 1px solid #a7f3d0;
            }
            .alert-error {
                background-color: #fee2e2;
                color: #991b1b;
                border: 1px solid #fecaca;
            }
            .alert-info {
                background-color: #e0f2fe;
                color: #075985;
                border: 1px solid #bae6fd;
            }

            /* Cards & Grids */
            .card {
                background: var(--card-bg);
                border-radius: var(--radius);
                border: 1px solid var(--border-color);
                box-shadow: var(--shadow-sm);
                padding: 20px;
                transition: transform 0.2s, box-shadow 0.2s;
            }
            .card-hover:hover {
                transform: translateY(-2px);
                box-shadow: var(--shadow-md);
            }

            /* Forms (Touch-optimized inputs) */
            .form-group {
                margin-bottom: 14px;
            }
            .form-label {
                display: block;
                font-size: 0.875rem;
                font-weight: 600;
                color: var(--secondary);
                margin-bottom: 6px;
            }
            .form-control {
                width: 100%;
                min-height: 44px;
                padding: 10px 14px;
                border: 1px solid var(--border-color);
                border-radius: 10px;
                font-size: 16px; /* 16px minimum prevents iOS Safari from auto-zooming */
                background-color: #ffffff;
                transition: border-color 0.2s;
            }
            .form-control:focus {
                outline: none;
                border-color: var(--primary);
                box-shadow: 0 0 0 3px var(--primary-light);
            }

            /* Badges */
            .badge {
                display: inline-flex;
                align-items: center;
                padding: 4px 10px;
                border-radius: 20px;
                font-size: 0.75rem;
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }
            .badge-success { background: #d1fae5; color: #065f46; }
            .badge-danger { background: #fee2e2; color: #991b1b; }
            .badge-warning { background: #fef3c7; color: #92400e; }
            .badge-info { background: #e0f2fe; color: #075985; }

            /* Footer */
            .footer {
                background: #ffffff;
                border-top: 1px solid var(--border-color);
                padding: 24px 0;
                margin-top: 30px;
                text-align: center;
                font-size: 0.875rem;
            }
            .footer-content p {
                margin: 4px 0;
            }

            /* Mobile Bottom Navigation (Hidden on Desktop) */
            .mobile-bottom-nav {
                display: none;
            }

            /* Category Pills Touch Scroll */
            .category-scroll-container {
                display: flex;
                gap: 8px;
                overflow-x: auto;
                padding-bottom: 6px;
                -webkit-overflow-scrolling: touch;
                scrollbar-width: none;
            }
            .category-scroll-container::-webkit-scrollbar {
                display: none;
            }

            /* Responsive Media Queries for Mobile Phones */
            @media (max-width: 768px) {
                .desktop-only {
                    display: none !important;
                }

                .container {
                    padding: 0 12px;
                }

                .card {
                    padding: 16px;
                    border-radius: 12px;
                }

                .main-content {
                    padding: 14px 0 85px 0; /* Space for fixed bottom bar */
                }

                .nav-container {
                    padding: 10px 14px;
                }

                .brand-title {
                    font-size: 1.1rem;
                }
                .brand-icon {
                    width: 36px;
                    height: 36px;
                    font-size: 1.5rem;
                }

                /* Mobile Bottom Navigation Bar */
                .mobile-bottom-nav {
                    display: flex;
                    position: fixed;
                    bottom: 0;
                    left: 0;
                    right: 0;
                    height: 60px;
                    background: #ffffff;
                    border-top: 1px solid var(--border-color);
                    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.08);
                    z-index: 1000;
                    align-items: center;
                    justify-content: space-around;
                    padding-bottom: env(safe-area-inset-bottom, 0px);
                }

                .mobile-nav-item {
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    justify-content: center;
                    text-decoration: none;
                    color: var(--text-muted);
                    font-size: 0.72rem;
                    font-weight: 600;
                    padding: 6px 12px;
                    min-width: 55px;
                    transition: color 0.15s;
                }

                .mobile-nav-item.active {
                    color: var(--primary);
                }

                .mobile-nav-icon {
                    font-size: 1.35rem;
                    line-height: 1.2;
                }

                .mobile-nav-label {
                    margin-top: 2px;
                }

                .mobile-cart-badge {
                    position: absolute;
                    top: -6px;
                    right: -10px;
                    background: var(--primary);
                    color: #ffffff;
                    font-size: 0.65rem;
                    font-weight: 800;
                    padding: 1px 5px;
                    border-radius: 10px;
                    min-width: 16px;
                    text-align: center;
                }

                /* Form Layouts on Mobile */
                .grid-responsive-2, .grid-responsive-3 {
                    grid-template-columns: 1fr !important;
                    gap: 10px !important;
                }

                .cart-responsive-grid {
                    grid-template-columns: 1fr !important;
                }
            }
            """;
    }
}
