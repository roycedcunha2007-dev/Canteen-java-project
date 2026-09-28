package com.canteen.view;

import com.canteen.config.AppConfig;
import com.canteen.model.OrderItem;
import com.canteen.model.User;

import java.util.List;

public class CartView {

    public static String render(List<OrderItem> cart, double total, User user) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div style=\"margin-bottom: 20px;\">\n");
        sb.append("  <h1 style=\"font-size: 1.6rem; font-weight: 800; color: var(--secondary);\">🛒 Your Food Cart</h1>\n");
        sb.append("  <p style=\"color: var(--text-muted); font-size: 0.9rem;\">Review meals, select payment, and place your order</p>\n");
        sb.append("</div>\n");

        if (cart == null || cart.isEmpty()) {
            sb.append("<div class=\"card\" style=\"text-align: center; padding: 48px 16px;\">\n");
            sb.append("  <span style=\"font-size: 3.5rem;\">🛒</span>\n");
            sb.append("  <h2 style=\"margin: 14px 0 6px; font-weight: 700; color: var(--secondary); font-size: 1.25rem;\">Your cart is currently empty</h2>\n");
            sb.append("  <p style=\"color: var(--text-muted); font-size: 0.9rem; max-width: 380px; margin: 0 auto;\">Hungry? Explore our hot dosas, thalis, fresh sandwiches, and chilled drinks!</p>\n");
            sb.append("  <a href=\"/menu\" class=\"btn btn-primary\" style=\"margin-top: 20px; padding: 12px 24px; font-size: 0.95rem;\">Browse Canteen Menu</a>\n");
            sb.append("</div>\n");
            return sb.toString();
        }

        sb.append("<div class=\"cart-responsive-grid\" style=\"display: grid; grid-template-columns: 2fr 1fr; gap: 20px; align-items: start;\">\n");

        // Left Column: Items List (Card based on mobile, table on desktop)
        sb.append("  <div class=\"card\" style=\"padding: 16px;\">\n");
        sb.append("    <div style=\"font-size: 0.8rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; margin-bottom: 12px;\">Selected Items</div>\n");
        sb.append("    <div style=\"display: flex; flex-direction: column; gap: 12px;\">\n");

        for (OrderItem item : cart) {
            sb.append("      <div style=\"display: flex; justify-content: space-between; align-items: center; padding: 12px; background: #f8fafc; border-radius: 10px; border: 1px solid var(--border-color); flex-wrap: wrap; gap: 10px;\">\n");

            // Left: Emoji + Name & Unit Price
            sb.append("        <div style=\"display: flex; align-items: center; gap: 10px; min-width: 140px; flex: 1;\">\n");
            sb.append("          <span style=\"font-size: 2rem;\">").append(item.getEmoji()).append("</span>\n");
            sb.append("          <div>\n");
            sb.append("            <div style=\"font-weight: 700; color: var(--secondary); font-size: 0.95rem; line-height: 1.2;\">").append(escape(item.getFoodName())).append("</div>\n");
            sb.append("            <div style=\"font-size: 0.8rem; color: var(--text-muted); margin-top: 2px;\">").append(item.getFormattedUnitPrice()).append(" each</div>\n");
            sb.append("          </div>\n");
            sb.append("        </div>\n");

            // Right: Quantity Stepper + Subtotal + Remove
            sb.append("        <div style=\"display: flex; align-items: center; gap: 12px; justify-content: flex-end;\">\n");

            // Touch-Friendly Quantity Stepper
            sb.append("          <div style=\"display: inline-flex; align-items: center; border: 1px solid var(--border-color); border-radius: 8px; overflow: hidden; background: #ffffff;\">\n");
            sb.append("            <form method=\"POST\" action=\"/cart/update\" style=\"margin: 0;\">\n");
            sb.append("              <input type=\"hidden\" name=\"foodItemId\" value=\"").append(item.getFoodItemId()).append("\">\n");
            sb.append("              <input type=\"hidden\" name=\"quantity\" value=\"").append(item.getQuantity() - 1).append("\">\n");
            sb.append("              <button type=\"submit\" style=\"border: none; background: #f1f5f9; width: 36px; height: 36px; cursor: pointer; font-size: 1.1rem; font-weight: bold; display: flex; align-items: center; justify-content: center;\">-</button>\n");
            sb.append("            </form>\n");

            sb.append("            <span style=\"width: 32px; text-align: center; font-weight: 700; font-size: 0.95rem;\">").append(item.getQuantity()).append("</span>\n");

            sb.append("            <form method=\"POST\" action=\"/cart/update\" style=\"margin: 0;\">\n");
            sb.append("              <input type=\"hidden\" name=\"foodItemId\" value=\"").append(item.getFoodItemId()).append("\">\n");
            sb.append("              <input type=\"hidden\" name=\"quantity\" value=\"").append(item.getQuantity() + 1).append("\">\n");
            sb.append("              <button type=\"submit\" style=\"border: none; background: #f1f5f9; width: 36px; height: 36px; cursor: pointer; font-size: 1.1rem; font-weight: bold; display: flex; align-items: center; justify-content: center;\">+</button>\n");
            sb.append("            </form>\n");
            sb.append("          </div>\n");

            // Subtotal
            sb.append("          <div style=\"font-weight: 800; color: var(--secondary); font-size: 1rem; min-width: 60px; text-align: right;\">")
              .append(item.getFormattedSubtotal()).append("</div>\n");

            // Remove Button
            sb.append("          <form method=\"POST\" action=\"/cart/remove\" style=\"margin: 0;\">\n");
            sb.append("            <input type=\"hidden\" name=\"foodItemId\" value=\"").append(item.getFoodItemId()).append("\">\n");
            sb.append("            <button type=\"submit\" class=\"btn btn-outline btn-sm\" style=\"color: #ef4444; border-color: #fecaca; min-height: 36px; width: 36px; padding: 0; display: flex; align-items: center; justify-content: center; font-size: 0.9rem;\" title=\"Remove\">✕</button>\n");
            sb.append("          </form>\n");

            sb.append("        </div>\n"); // End Right
            sb.append("      </div>\n"); // End Item Card
        }

        sb.append("    </div>\n");

        sb.append("    <div style=\"display: flex; justify-content: space-between; align-items: center; margin-top: 16px; padding-top: 12px; border-top: 1px solid var(--border-color);\">\n");
        sb.append("      <a href=\"/menu\" class=\"btn btn-outline btn-sm\">← Add More Items</a>\n");
        sb.append("      <form method=\"POST\" action=\"/cart/clear\" style=\"margin: 0;\">\n");
        sb.append("        <button type=\"submit\" class=\"btn btn-outline btn-sm\" style=\"color: #ef4444; border-color: #fecaca;\">Clear Cart</button>\n");
        sb.append("      </form>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");

        // Right Column: Checkout Bill & Payment Form
        sb.append("  <div class=\"card\" style=\"padding: 16px;\">\n");
        sb.append("    <h3 style=\"font-size: 1.15rem; font-weight: 700; color: var(--secondary); margin-bottom: 12px; border-bottom: 1px solid var(--border-color); padding-bottom: 10px;\">Order Bill Summary</h3>\n");

        sb.append("    <div style=\"display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 0.9rem; color: var(--text-muted);\">\n");
        sb.append("      <span>Items Total:</span>\n");
        sb.append("      <span>").append(String.format("₹%.2f", total)).append("</span>\n");
        sb.append("    </div>\n");

        sb.append("    <div style=\"display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 0.9rem; color: var(--text-muted);\">\n");
        sb.append("      <span>Student Discount:</span>\n");
        sb.append("      <span style=\"color: var(--accent); font-weight: 600;\">Free Ordering</span>\n");
        sb.append("    </div>\n");

        sb.append("    <div style=\"display: flex; justify-content: space-between; margin-bottom: 14px; font-size: 0.9rem; color: var(--text-muted);\">\n");
        sb.append("      <span>Counter Fee:</span>\n");
        sb.append("      <span style=\"color: var(--accent); font-weight: 600;\">₹0.00</span>\n");
        sb.append("    </div>\n");

        sb.append("    <div style=\"border-top: 2px dashed var(--border-color); padding-top: 12px; margin-bottom: 16px; display: flex; justify-content: space-between; align-items: baseline;\">\n");
        sb.append("      <span style=\"font-size: 1rem; font-weight: 700; color: var(--secondary);\">Total Payable:</span>\n");
        sb.append("      <span style=\"font-size: 1.5rem; font-weight: 800; color: var(--primary);\">").append(String.format("₹%.2f", total)).append("</span>\n");
        sb.append("    </div>\n");

        // Checkout form
        sb.append("    <form method=\"POST\" action=\"/order/place\">\n");

        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\">Payment Method</label>\n");
        sb.append("        <select name=\"paymentMethod\" class=\"form-control\" style=\"font-size: 0.9rem;\">\n");
        sb.append("          <option value=\"Pay at Canteen Counter\">💵 Pay at Counter (Cash/Card)</option>\n");
        sb.append("          <option value=\"UPI / QR at Pickup\">📱 Instant UPI QR at Counter</option>\n");
        sb.append("          <option value=\"Campus Smart Card\">💳 Student Campus ID Wallet</option>\n");
        sb.append("        </select>\n");
        sb.append("      </div>\n");

        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\">Cooking / Packaging Notes</label>\n");
        sb.append("        <input type=\"text\" name=\"notes\" class=\"form-control\" placeholder=\"e.g. Less spicy, parcel...\">\n");
        sb.append("      </div>\n");

        if (user != null) {
            sb.append("      <div style=\"background: #f8fafc; padding: 10px; border-radius: 8px; margin-bottom: 14px; font-size: 0.8rem; color: var(--text-muted);\">\n");
            sb.append("        Student: <strong>").append(escape(user.getFullName())).append("</strong> (Roll: ").append(escape(user.getRollNumber())).append(")\n");
            sb.append("      </div>\n");
            sb.append("      <button type=\"submit\" class=\"btn btn-primary btn-block\" style=\"font-size: 1rem; min-height: 48px; font-weight: 700;\">🚀 Place Canteen Order</button>\n");
        } else {
            sb.append("      <a href=\"/login\" class=\"btn btn-primary btn-block\" style=\"min-height: 46px;\">Sign In to Checkout</a>\n");
        }

        sb.append("    </form>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
