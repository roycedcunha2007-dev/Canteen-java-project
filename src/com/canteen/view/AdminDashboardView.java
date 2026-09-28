package com.canteen.view;

import com.canteen.config.AppConfig;
import com.canteen.model.Order;
import com.canteen.model.OrderStatus;

import java.util.List;
import java.util.Map;

public class AdminDashboardView {

    public static String render(double totalRevenue, double todayRevenue, int todayOrders, int activeOrders, int completedOrders, Map<String, Integer> topSelling, List<Order> recentOrders) {
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append("<div style=\"display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; flex-wrap: wrap; gap: 12px;\">\n");
        sb.append("  <div>\n");
        sb.append("    <h1 style=\"font-size: 1.8rem; font-weight: 800; color: var(--secondary);\">👨‍🍳 Canteen Management Dashboard</h1>\n");
        sb.append("    <p style=\"color: var(--text-muted);\">Real-time operational overview, sales records, and active kitchen queue</p>\n");
        sb.append("  </div>\n");
        sb.append("  <div style=\"display: flex; gap: 10px;\">\n");
        sb.append("    <a href=\"/admin/orders\" class=\"btn btn-primary btn-sm\">🔔 Live Kitchen Orders (").append(activeOrders).append(")</a>\n");
        sb.append("    <a href=\"/admin/menu\" class=\"btn btn-secondary btn-sm\">📋 Manage Menu Items</a>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        // 4 Key Metric Cards
        sb.append("<div style=\"display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px; margin-bottom: 30px;\">\n");

        // Today's Sales
        sb.append("  <div class=\"card\" style=\"border-left: 4px solid var(--primary);\">\n");
        sb.append("    <div style=\"font-size: 0.8rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;\">Today's Revenue</div>\n");
        sb.append("    <div style=\"font-size: 1.8rem; font-weight: 800; color: var(--secondary); margin: 6px 0;\">").append(String.format("₹%.2f", todayRevenue)).append("</div>\n");
        sb.append("    <div style=\"font-size: 0.75rem; color: var(--accent); font-weight: 600;\">Total Lifetime: ").append(String.format("₹%.2f", totalRevenue)).append("</div>\n");
        sb.append("  </div>\n");

        // Active Orders in Kitchen
        sb.append("  <div class=\"card\" style=\"border-left: 4px solid #3b82f6;\">\n");
        sb.append("    <div style=\"font-size: 0.8rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;\">In Kitchen / Ready</div>\n");
        sb.append("    <div style=\"font-size: 1.8rem; font-weight: 800; color: #1d4ed8; margin: 6px 0;\">").append(activeOrders).append("</div>\n");
        sb.append("    <div style=\"font-size: 0.75rem; color: var(--text-muted);\">Requires staff action</div>\n");
        sb.append("  </div>\n");

        // Today's Orders Count
        sb.append("  <div class=\"card\" style=\"border-left: 4px solid #f59e0b;\">\n");
        sb.append("    <div style=\"font-size: 0.8rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;\">Orders Placed Today</div>\n");
        sb.append("    <div style=\"font-size: 1.8rem; font-weight: 800; color: #b45309; margin: 6px 0;\">").append(todayOrders).append("</div>\n");
        sb.append("    <div style=\"font-size: 0.75rem; color: var(--text-muted);\">Processed through system</div>\n");
        sb.append("  </div>\n");

        // Completed Orders Count
        sb.append("  <div class=\"card\" style=\"border-left: 4px solid #10b981;\">\n");
        sb.append("    <div style=\"font-size: 0.8rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;\">Completed & Picked Up</div>\n");
        sb.append("    <div style=\"font-size: 1.8rem; font-weight: 800; color: #047857; margin: 6px 0;\">").append(completedOrders).append("</div>\n");
        sb.append("    <div style=\"font-size: 0.75rem; color: #10b981; font-weight: 600;\">Successful orders</div>\n");
        sb.append("  </div>\n");

        sb.append("</div>\n");

        // Two Column Section: Top Selling Items + Recent Orders (responsive)
        sb.append("<div class=\"cart-responsive-grid\" style=\"display: grid; grid-template-columns: 1fr 2fr; gap: 20px; align-items: start;\">\n");

        // Left: Popular / Top-Selling Leaderboard
        sb.append("  <div class=\"card\">\n");
        sb.append("    <h3 style=\"font-size: 1.15rem; font-weight: 700; color: var(--secondary); margin-bottom: 14px;\">🔥 Popular Canteen Items</h3>\n");
        if (topSelling.isEmpty()) {
            sb.append("    <p style=\"color: var(--text-muted); font-size: 0.9rem;\">No order sales recorded yet.</p>\n");
        } else {
            sb.append("    <div style=\"display: flex; flex-direction: column; gap: 10px;\">\n");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : topSelling.entrySet()) {
                sb.append("      <div style=\"display: flex; justify-content: space-between; align-items: center; padding: 8px 10px; background: #f8fafc; border-radius: 8px;\">\n");
                sb.append("        <div style=\"display: flex; align-items: center; gap: 8px;\">\n");
                sb.append("          <span style=\"font-weight: 800; color: var(--text-muted); font-size: 0.85rem;\">#").append(rank++).append("</span>\n");
                sb.append("          <span style=\"font-weight: 600; font-size: 0.9rem; color: var(--secondary);\">").append(escape(entry.getKey())).append("</span>\n");
                sb.append("        </div>\n");
                sb.append("        <span class=\"badge badge-info\">").append(entry.getValue()).append(" sold</span>\n");
                sb.append("      </div>\n");
            }
            sb.append("    </div>\n");
        }
        sb.append("  </div>\n");

        // Right: Recent Incoming Orders
        sb.append("  <div class=\"card\">\n");
        sb.append("    <div style=\"display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;\">\n");
        sb.append("      <h3 style=\"font-size: 1.15rem; font-weight: 700; color: var(--secondary);\">🕒 Recent Orders</h3>\n");
        sb.append("      <a href=\"/admin/orders\" style=\"font-size: 0.85rem; color: var(--primary); font-weight: 600; text-decoration: none;\">View All →</a>\n");
        sb.append("    </div>\n");

        if (recentOrders.isEmpty()) {
            sb.append("    <p style=\"color: var(--text-muted); font-size: 0.9rem;\">No recent orders.</p>\n");
        } else {
            sb.append("    <div style=\"overflow-x: auto; -webkit-overflow-scrolling: touch;\">\n");
            sb.append("      <table style=\"width: 100%; min-width: 480px; border-collapse: collapse; font-size: 0.9rem;\">\n");
            sb.append("        <thead>\n");
            sb.append("          <tr style=\"border-bottom: 2px solid var(--border-color); text-align: left;\">\n");
            sb.append("            <th style=\"padding: 8px; color: var(--text-muted); font-size: 0.75rem; text-transform: uppercase;\">Token & Order</th>\n");
            sb.append("            <th style=\"padding: 8px; color: var(--text-muted); font-size: 0.75rem; text-transform: uppercase;\">Student</th>\n");
            sb.append("            <th style=\"padding: 8px; color: var(--text-muted); font-size: 0.75rem; text-transform: uppercase;\">Amount</th>\n");
            sb.append("            <th style=\"padding: 8px; color: var(--text-muted); font-size: 0.75rem; text-transform: uppercase;\">Status</th>\n");
            sb.append("            <th style=\"padding: 8px; color: var(--text-muted); font-size: 0.75rem; text-transform: uppercase; text-align: right;\">Action</th>\n");
            sb.append("          </tr>\n");
            sb.append("        </thead>\n");
            sb.append("        <tbody>\n");

            int count = 0;
            for (Order o : recentOrders) {
                if (count++ >= 8) break;
                sb.append("          <tr style=\"border-bottom: 1px solid var(--border-color);\">\n");
                sb.append("            <td style=\"padding: 10px 8px;\">\n");
                sb.append("              <strong style=\"color: #c2410c;\">").append(o.getPickupToken()).append("</strong>\n");
                sb.append("              <div style=\"font-size: 0.75rem; color: var(--text-muted);\">").append(o.getOrderNumber()).append("</div>\n");
                sb.append("            </td>\n");

                sb.append("            <td style=\"padding: 10px 8px;\">\n");
                sb.append("              <div style=\"font-weight: 600;\">").append(escape(o.getStudentName())).append("</div>\n");
                sb.append("              <div style=\"font-size: 0.75rem; color: var(--text-muted);\">Roll: ").append(escape(o.getStudentRollNo())).append("</div>\n");
                sb.append("            </td>\n");

                sb.append("            <td style=\"padding: 10px 8px; font-weight: 700;\">").append(o.getFormattedTotal()).append("</td>\n");

                sb.append("            <td style=\"padding: 10px 8px;\">\n");
                sb.append("              <span class=\"badge\" style=\"background: ").append(o.getStatus().getHexColor()).append("20; color: ").append(o.getStatus().getHexColor()).append("; border: 1px solid ").append(o.getStatus().getHexColor()).append("40;\">")
                  .append(o.getStatus().getDisplayName()).append("</span>\n");
                sb.append("            </td>\n");

                sb.append("            <td style=\"padding: 10px 8px; text-align: right;\">\n");
                sb.append("              <a href=\"/admin/orders\" class=\"btn btn-outline btn-sm\" style=\"font-size: 0.8rem;\">Manage</a>\n");
                sb.append("            </td>\n");
                sb.append("          </tr>\n");
            }

            sb.append("        </tbody>\n");
            sb.append("      </table>\n");
            sb.append("    </div>\n");
        }

        sb.append("  </div>\n");
        sb.append("</div>\n");

        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
