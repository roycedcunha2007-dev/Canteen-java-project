package com.canteen.view;

import com.canteen.model.Order;
import com.canteen.model.OrderItem;
import com.canteen.model.OrderStatus;

import java.util.List;

public class AdminOrderView {

    public static String render(List<Order> orders, String filter) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div style=\"display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 10px;\">\n");
        sb.append("  <div>\n");
        sb.append("    <h1 style=\"font-size: 1.6rem; font-weight: 800; color: var(--secondary);\">🔔 Kitchen Orders Queue</h1>\n");
        sb.append("    <p style=\"color: var(--text-muted); font-size: 0.88rem;\">Real-time incoming student orders and kitchen workflow</p>\n");
        sb.append("  </div>\n");
        sb.append("  <div style=\"display: flex; gap: 8px; align-items: center;\">\n");
        sb.append("    <a href=\"/admin/orders\" class=\"btn btn-outline btn-sm\" style=\"min-height: 36px;\">🔄 Refresh</a>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        // Touch-scrollable Filter Pills
        String currentFilter = (filter != null && !filter.isEmpty()) ? filter : "active";
        sb.append("<div class=\"category-scroll-container\" style=\"margin-bottom: 20px;\">\n");
        sb.append("  <a href=\"/admin/orders?filter=active\" class=\"btn btn-sm ").append("active".equals(currentFilter) ? "btn-secondary" : "btn-outline").append("\" style=\"white-space: nowrap; border-radius: 20px;\">🔥 Active Orders</a>\n");
        sb.append("  <a href=\"/admin/orders?filter=all\" class=\"btn btn-sm ").append("all".equals(currentFilter) ? "btn-secondary" : "btn-outline").append("\" style=\"white-space: nowrap; border-radius: 20px;\">All Orders</a>\n");
        sb.append("  <a href=\"/admin/orders?filter=placed\" class=\"btn btn-sm ").append("placed".equals(currentFilter) ? "btn-secondary" : "btn-outline").append("\" style=\"white-space: nowrap; border-radius: 20px;\">📝 Placed</a>\n");
        sb.append("  <a href=\"/admin/orders?filter=preparing\" class=\"btn btn-sm ").append("preparing".equals(currentFilter) ? "btn-secondary" : "btn-outline").append("\" style=\"white-space: nowrap; border-radius: 20px;\">👨‍🍳 In Kitchen</a>\n");
        sb.append("  <a href=\"/admin/orders?filter=ready\" class=\"btn btn-sm ").append("ready".equals(currentFilter) ? "btn-secondary" : "btn-outline").append("\" style=\"white-space: nowrap; border-radius: 20px;\">🔔 Ready for Pickup</a>\n");
        sb.append("  <a href=\"/admin/orders?filter=completed\" class=\"btn btn-sm ").append("completed".equals(currentFilter) ? "btn-secondary" : "btn-outline").append("\" style=\"white-space: nowrap; border-radius: 20px;\">✅ Completed</a>\n");
        sb.append("</div>\n");

        if (orders == null || orders.isEmpty()) {
            sb.append("<div class=\"card\" style=\"text-align: center; padding: 48px 16px;\">\n");
            sb.append("  <span style=\"font-size: 3rem;\">🎉</span>\n");
            sb.append("  <h2 style=\"margin: 14px 0 6px; font-weight: 700; color: var(--secondary); font-size: 1.25rem;\">No orders in this view</h2>\n");
            sb.append("  <p style=\"color: var(--text-muted); font-size: 0.9rem;\">There are no orders matching this filter right now.</p>\n");
            sb.append("</div>\n");
            return sb.toString();
        }

        // Order Cards Grid
        sb.append("<div style=\"display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 16px;\">\n");

        for (Order order : orders) {
            sb.append("<div class=\"card\" style=\"border-top: 5px solid ").append(order.getStatus().getHexColor()).append("; display: flex; flex-direction: column; justify-content: space-between; padding: 16px;\">\n");

            // Card Top
            sb.append("  <div>\n");
            sb.append("    <div style=\"display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 10px;\">\n");
            sb.append("      <div>\n");
            sb.append("        <span style=\"background: #fff7ed; border: 1px solid #fdba74; color: #c2410c; font-size: 1.3rem; font-weight: 900; padding: 3px 10px; border-radius: 8px; display: inline-block;\">")
              .append(order.getPickupToken()).append("</span>\n");
            sb.append("        <div style=\"font-size: 0.75rem; color: var(--text-muted); margin-top: 4px;\">").append(order.getOrderNumber()).append(" &bull; ").append(order.getFormattedOrderTime()).append("</div>\n");
            sb.append("      </div>\n");

            sb.append("      <span class=\"badge\" style=\"background: ").append(order.getStatus().getHexColor()).append("20; color: ").append(order.getStatus().getHexColor()).append("; border: 1px solid ").append(order.getStatus().getHexColor()).append("40;\">")
              .append(order.getStatus().getDisplayName()).append("</span>\n");
            sb.append("    </div>\n");

            // Student Info
            sb.append("    <div style=\"background: #f8fafc; padding: 8px 10px; border-radius: 8px; margin-bottom: 10px; font-size: 0.82rem;\">\n");
            sb.append("      <div><strong>🎓 Student:</strong> ").append(escape(order.getStudentName())).append(" (").append(escape(order.getStudentRollNo())).append(")</div>\n");
            if (order.getStudentPhone() != null && !order.getStudentPhone().isEmpty()) {
                sb.append("      <div style=\"color: var(--text-muted); margin-top: 2px;\">📞 ").append(escape(order.getStudentPhone())).append(" &bull; 💳 ").append(escape(order.getPaymentMethod())).append("</div>\n");
            }
            sb.append("    </div>\n");

            // Items List
            sb.append("    <div style=\"margin-bottom: 12px;\">\n");
            sb.append("      <div style=\"font-size: 0.72rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; margin-bottom: 4px;\">Items to Cook (").append(order.getTotalItemCount()).append(")</div>\n");
            sb.append("      <div style=\"display: flex; flex-direction: column; gap: 4px;\">\n");
            for (OrderItem item : order.getItems()) {
                sb.append("        <div style=\"display: flex; justify-content: space-between; font-size: 0.85rem; padding: 3px 0; border-bottom: 1px dashed var(--border-color);\">\n");
                sb.append("          <span>").append(item.getEmoji()).append(" <strong>").append(escape(item.getFoodName())).append("</strong></span>\n");
                sb.append("          <span style=\"font-weight: 700;\">× ").append(item.getQuantity()).append("</span>\n");
                sb.append("        </div>\n");
            }
            sb.append("      </div>\n");

            if (order.getNotes() != null && !order.getNotes().trim().isEmpty()) {
                sb.append("      <div style=\"background: #fef3c7; color: #92400e; padding: 6px 8px; border-radius: 6px; font-size: 0.78rem; margin-top: 6px;\">\n");
                sb.append("        ⚠️ <strong>Note:</strong> ").append(escape(order.getNotes())).append("\n");
                sb.append("      </div>\n");
            }

            sb.append("    </div>\n");

            sb.append("    <div style=\"display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 14px;\">\n");
            sb.append("      <span style=\"font-size: 0.8rem; color: var(--text-muted);\">Total:</span>\n");
            sb.append("      <span style=\"font-size: 1.25rem; font-weight: 800; color: var(--secondary);\">").append(order.getFormattedTotal()).append("</span>\n");
            sb.append("    </div>\n");

            sb.append("  </div>\n"); // End Card Top

            // Status Update Touch Transition Buttons
            sb.append("  <div style=\"border-top: 1px solid var(--border-color); padding-top: 12px; margin-top: 6px;\">\n");

            if (order.getStatus() == OrderStatus.PLACED) {
                sb.append("    <form method=\"POST\" action=\"/admin/orders/status\" style=\"margin-bottom: 6px;\">\n");
                sb.append("      <input type=\"hidden\" name=\"orderId\" value=\"").append(order.getId()).append("\">\n");
                sb.append("      <input type=\"hidden\" name=\"newStatus\" value=\"PREPARING\">\n");
                sb.append("      <button type=\"submit\" class=\"btn btn-primary btn-block\" style=\"min-height: 44px;\">👨‍🍳 Start Cooking</button>\n");
                sb.append("    </form>\n");
            } else if (order.getStatus() == OrderStatus.PREPARING) {
                sb.append("    <form method=\"POST\" action=\"/admin/orders/status\" style=\"margin-bottom: 6px;\">\n");
                sb.append("      <input type=\"hidden\" name=\"orderId\" value=\"").append(order.getId()).append("\">\n");
                sb.append("      <input type=\"hidden\" name=\"newStatus\" value=\"READY\">\n");
                sb.append("      <button type=\"submit\" class=\"btn btn-success btn-block\" style=\"min-height: 44px;\">🔔 Mark Ready for Counter</button>\n");
                sb.append("    </form>\n");
            } else if (order.getStatus() == OrderStatus.READY) {
                sb.append("    <form method=\"POST\" action=\"/admin/orders/status\" style=\"margin-bottom: 6px;\">\n");
                sb.append("      <input type=\"hidden\" name=\"orderId\" value=\"").append(order.getId()).append("\">\n");
                sb.append("      <input type=\"hidden\" name=\"newStatus\" value=\"COMPLETED\">\n");
                sb.append("      <button type=\"submit\" class=\"btn btn-secondary btn-block\" style=\"min-height: 44px;\">✅ Mark Picked Up & Done</button>\n");
                sb.append("    </form>\n");
            }

            // Cancel action
            if (order.getStatus() != OrderStatus.COMPLETED && order.getStatus() != OrderStatus.CANCELLED) {
                sb.append("    <form method=\"POST\" action=\"/admin/orders/status\" onsubmit=\"return confirm('Cancel this order?');\">\n");
                sb.append("      <input type=\"hidden\" name=\"orderId\" value=\"").append(order.getId()).append("\">\n");
                sb.append("      <input type=\"hidden\" name=\"newStatus\" value=\"CANCELLED\">\n");
                sb.append("      <button type=\"submit\" class=\"btn btn-outline btn-block btn-sm\" style=\"color: #ef4444; border-color: #fecaca; min-height: 38px;\">Cancel Order</button>\n");
                sb.append("    </form>\n");
            } else {
                sb.append("    <div style=\"text-align: center; font-size: 0.75rem; color: var(--text-muted); padding: 4px 0;\">Order completed</div>\n");
            }

            sb.append("  </div>\n");

            sb.append("</div>\n");
        }

        sb.append("</div>\n");

        // Auto refresh
        sb.append("<script>\n");
        sb.append("  setTimeout(function() {\n");
        sb.append("    window.location.reload();\n");
        sb.append("  }, 10000);\n");
        sb.append("</script>\n");

        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
