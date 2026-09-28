package com.canteen.view;

import com.canteen.config.AppConfig;
import com.canteen.model.Category;
import com.canteen.model.FoodItem;
import com.canteen.model.User;

import java.util.List;

public class MenuView {

    public static String render(List<FoodItem> items, Category selectedCategory, String searchQuery, User user, int cartCount, double cartTotal) {
        StringBuilder sb = new StringBuilder();

        // Mobile Optimized Hero Banner
        sb.append("<div style=\"background: linear-gradient(135deg, #ea580c 0%, #c2410c 100%); color: white; border-radius: var(--radius); padding: 24px 20px; margin-bottom: 20px; box-shadow: var(--shadow-md); position: relative; overflow: hidden;\">\n");
        sb.append("  <div style=\"position: relative; z-index: 2; max-width: 600px;\">\n");
        sb.append("    <span style=\"background: rgba(255,255,255,0.22); padding: 3px 10px; border-radius: 20px; font-size: 0.75rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;\">🚀 Instant Mobile Ordering</span>\n");
        sb.append("    <h1 style=\"font-size: 1.6rem; font-weight: 800; margin: 10px 0 6px; line-height: 1.25;\">College Canteen Menu</h1>\n");
        sb.append("    <p style=\"font-size: 0.92rem; opacity: 0.95; line-height: 1.4;\">Order ahead from your phone, get your pickup token, and skip break-hour counter queues!</p>\n");
        sb.append("  </div>\n");
        sb.append("  <span style=\"position: absolute; right: 10px; bottom: -15px; font-size: 6rem; opacity: 0.15; user-select: none;\">🍕</span>\n");
        sb.append("</div>\n");

        // Mobile Floating View Cart Bar (if cart has items)
        if (cartCount > 0) {
            sb.append("<div class=\"mobile-floating-cart-bar\" style=\"position: sticky; top: 62px; z-index: 95; margin-bottom: 16px; background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%); color: #ffffff; padding: 12px 16px; border-radius: 12px; display: flex; justify-content: space-between; align-items: center; box-shadow: var(--shadow-md);\">\n");
            sb.append("  <div style=\"display: flex; align-items: center; gap: 10px;\">\n");
            sb.append("    <span style=\"background: #ea580c; width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 0.95rem; font-weight: 800;\">").append(cartCount).append("</span>\n");
            sb.append("    <div>\n");
            sb.append("      <div style=\"font-weight: 700; font-size: 0.95rem;\">").append(String.format("₹%.2f", cartTotal)).append("</div>\n");
            sb.append("      <div style=\"font-size: 0.75rem; color: #94a3b8;\">").append(cartCount).append(" item(s) selected</div>\n");
            sb.append("    </div>\n");
            sb.append("  </div>\n");
            sb.append("  <a href=\"/cart\" class=\"btn btn-primary btn-sm\" style=\"padding: 8px 16px; font-size: 0.9rem; font-weight: 700;\">View Cart 🛒 →</a>\n");
            sb.append("</div>\n");
        }

        // Search & Filter Card
        sb.append("<div class=\"card\" style=\"margin-bottom: 20px; padding: 16px;\">\n");
        sb.append("  <form method=\"GET\" action=\"/menu\" style=\"display: flex; gap: 8px; flex-wrap: wrap; align-items: center;\">\n");
        sb.append("    <div style=\"flex: 1; min-width: 200px;\">\n");
        sb.append("      <input type=\"text\" name=\"q\" class=\"form-control\" placeholder=\"🔍 Search dosa, biryani, chai...\" value=\"").append(escape(searchQuery != null ? searchQuery : "")).append("\">\n");
        if (selectedCategory != null && selectedCategory != Category.ALL) {
            sb.append("      <input type=\"hidden\" name=\"category\" value=\"").append(selectedCategory.name()).append("\">\n");
        }
        sb.append("    </div>\n");
        sb.append("    <button type=\"submit\" class=\"btn btn-primary\" style=\"min-width: 80px;\">Search</button>\n");
        if ((searchQuery != null && !searchQuery.isEmpty()) || (selectedCategory != null && selectedCategory != Category.ALL)) {
            sb.append("    <a href=\"/menu\" class=\"btn btn-outline\" style=\"min-width: 70px;\">Clear</a>\n");
        }
        sb.append("  </form>\n");

        // Touch-Friendly Smooth Horizontal Category Pills
        sb.append("  <div class=\"category-scroll-container\" style=\"margin-top: 12px; padding-top: 12px; border-top: 1px solid var(--border-color);\">\n");
        for (Category cat : Category.values()) {
            boolean isActive = (selectedCategory == cat) || (selectedCategory == null && cat == Category.ALL);
            String url = "/menu?category=" + cat.name() + (searchQuery != null && !searchQuery.isEmpty() ? "&q=" + escape(searchQuery) : "");
            sb.append("    <a href=\"").append(url).append("\" class=\"btn btn-sm ").append(isActive ? "btn-secondary" : "btn-outline").append("\" style=\"white-space: nowrap; border-radius: 20px; padding: 6px 14px; font-size: 0.85rem;\">")
              .append(cat.getDisplayName()).append("</a>\n");
        }
        sb.append("  </div>\n");
        sb.append("</div>\n");

        // Results Section Header
        sb.append("<div style=\"display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;\">\n");
        sb.append("  <h2 style=\"font-size: 1.25rem; font-weight: 700; color: var(--secondary);\">\n");
        sb.append("    ").append(selectedCategory != null ? selectedCategory.getDisplayName() : "All Items");
        sb.append("    <span style=\"font-size: 0.85rem; font-weight: 500; color: var(--text-muted); margin-left: 6px;\">(").append(items.size()).append(")</span>\n");
        sb.append("  </h2>\n");
        sb.append("</div>\n");

        // Food Items Grid (Responsive 1-col on small phones, 2-col on tablets, 3-col on desktop)
        if (items.isEmpty()) {
            sb.append("<div class=\"card\" style=\"text-align: center; padding: 40px 16px;\">\n");
            sb.append("  <span style=\"font-size: 3rem;\">🍲</span>\n");
            sb.append("  <h3 style=\"margin-top: 10px; font-weight: 700; font-size: 1.15rem;\">No food items found</h3>\n");
            sb.append("  <p style=\"color: var(--text-muted); font-size: 0.9rem; margin-top: 4px;\">Try clearing your search query or switching categories.</p>\n");
            sb.append("  <a href=\"/menu\" class=\"btn btn-primary\" style=\"margin-top: 14px;\">View Full Menu</a>\n");
            sb.append("</div>\n");
        } else {
            sb.append("<div style=\"display: grid; grid-template-columns: repeat(auto-fill, minmax(260px, 1fr)); gap: 16px;\">\n");
            for (FoodItem item : items) {
                sb.append("<div class=\"card card-hover\" style=\"display: flex; flex-direction: column; justify-content: space-between; position: relative;\">\n");

                // Availability Badge
                sb.append("  <div style=\"position: absolute; top: 14px; right: 14px;\">\n");
                if (item.isAvailable()) {
                    sb.append("    <span class=\"badge badge-success\">In Stock</span>\n");
                } else {
                    sb.append("    <span class=\"badge badge-danger\">Sold Out</span>\n");
                }
                sb.append("  </div>\n");

                // Card Content
                sb.append("  <div>\n");
                sb.append("    <div style=\"display: flex; align-items: center; gap: 12px; margin-bottom: 10px;\">\n");
                sb.append("      <div style=\"font-size: 2.4rem; background: #fff7ed; width: 60px; height: 60px; border-radius: 14px; display: flex; align-items: center; justify-content: center; flex-shrink: 0;\">")
                  .append(item.getEmoji()).append("</div>\n");
                sb.append("      <div style=\"overflow: hidden;\">\n");
                sb.append("        <span style=\"font-size: 0.72rem; font-weight: 700; color: var(--primary); text-transform: uppercase;\">").append(item.getCategory().getDisplayName()).append("</span>\n");
                sb.append("        <h3 style=\"font-size: 1.05rem; font-weight: 700; color: var(--secondary); margin: 2px 0 0; line-height: 1.25;\">").append(escape(item.getName())).append("</h3>\n");
                sb.append("      </div>\n");
                sb.append("    </div>\n");
                sb.append("    <p style=\"font-size: 0.84rem; color: var(--text-muted); margin-bottom: 12px; min-height: 38px; line-height: 1.4;\">").append(escape(item.getDescription())).append("</p>\n");
                sb.append("  </div>\n");

                // Price & Touch Action Button
                sb.append("  <div style=\"border-top: 1px solid var(--border-color); padding-top: 12px; margin-top: 8px;\">\n");
                sb.append("    <div style=\"display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 10px;\">\n");
                sb.append("      <span style=\"font-size: 1.3rem; font-weight: 800; color: var(--secondary);\">").append(item.getFormattedPrice()).append("</span>\n");
                sb.append("      <span style=\"font-size: 0.78rem; color: var(--text-muted);\">⏱️ ~").append(item.getPrepTimeMinutes()).append(" mins</span>\n");
                sb.append("    </div>\n");

                if (item.isAvailable()) {
                    sb.append("    <form method=\"POST\" action=\"/cart/add\" style=\"display: flex; gap: 8px;\">\n");
                    sb.append("      <input type=\"hidden\" name=\"foodItemId\" value=\"").append(item.getId()).append("\">\n");
                    sb.append("      <input type=\"hidden\" name=\"quantity\" value=\"1\">\n");
                    sb.append("      <button type=\"submit\" class=\"btn btn-primary btn-block\" style=\"font-size: 0.95rem; min-height: 44px;\">➕ Add to Cart</button>\n");
                    sb.append("    </form>\n");
                } else {
                    sb.append("    <button class=\"btn btn-outline btn-block\" disabled style=\"min-height: 44px; font-size: 0.9rem;\">Currently Unavailable</button>\n");
                }

                sb.append("  </div>\n");
                sb.append("</div>\n");
            }
            sb.append("</div>\n");
        }

        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
