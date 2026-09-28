package com.canteen.view;

import com.canteen.model.Order;
import com.canteen.model.OrderItem;
import com.canteen.model.OrderStatus;

import java.util.List;

public class StudentOrderView {

    public static String render(List<Order> orders) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div style=\"display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 10px;\">\n");
        sb.append("  <div>\n");
        sb.append("    <h1 style=\"font-size: 1.6rem; font-weight: 800; color: var(--secondary);\">📦 My Orders & Live Status</h1>\n");
        sb.append("    <p style=\"color: var(--text-muted); font-size: 0.88rem;\">Flash your pickup token at Counter 1 when food is ready</p>\n");
        sb.append("  </div>\n");
        sb.append("  <div>\n");
        sb.append("    <a href=\"/my-orders\" class=\"btn btn-outline btn-sm\" style=\"min-height: 36px;\">🔄 Refresh</a>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        if (orders == null || orders.isEmpty()) {
            sb.append("<div class=\"card\" style=\"text-align: center; padding: 48px 16px;\">\n");
            sb.append("  <span style=\"font-size: 3.5rem;\">🍽️</span>\n");
            sb.append("  <h2 style=\"margin: 14px 0 6px; font-weight: 700; color: var(--secondary); font-size: 1.25rem;\">No orders yet</h2>\n");
            sb.append("  <p style=\"color: var(--text-muted); font-size: 0.9rem; max-width: 380px; margin: 0 auto;\">You haven't ordered anything yet today. Browse our menu to skip the canteen break queues!</p>\n");
            sb.append("  <a href=\"/menu\" class=\"btn btn-primary\" style=\"margin-top: 20px;\">Browse Canteen Menu</a>\n");
            sb.append("</div>\n");
            return sb.toString();
        }

        sb.append("<div style=\"display: flex; flex-direction: column; gap: 20px;\">\n");

        for (Order order : orders) {
            sb.append("<div class=\"card\" style=\"border-left: 6px solid ").append(order.getStatus().getHexColor()).append("; padding: 18px;\">\n");

            // Header row with Token & Order Info
            sb.append("  <div style=\"display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 12px; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px;\">\n");
            sb.append("    <div>\n");
            sb.append("      <div style=\"display: flex; align-items: center; gap: 8px; flex-wrap: wrap;\">\n");
            sb.append("        <span style=\"font-size: 1.15rem; font-weight: 800; color: var(--secondary);\">").append(order.getOrderNumber()).append("</span>\n");
            sb.append("        <span class=\"badge\" style=\"background: ").append(order.getStatus().getHexColor()).append("20; color: ").append(order.getStatus().getHexColor()).append("; border: 1px solid ").append(order.getStatus().getHexColor()).append("40;\">")
              .append(order.getStatus().getDisplayName()).append("</span>\n");
            sb.append("      </div>\n");
            sb.append("      <div style=\"font-size: 0.8rem; color: var(--text-muted); margin-top: 4px;\">\n");
            sb.append("        <span>🕒 ").append(order.getFormattedOrderTime()).append("</span> &bull; <span>").append(escape(order.getPaymentMethod())).append("</span>\n");
            sb.append("      </div>\n");
            sb.append("    </div>\n");

            // Prominent Pickup Token Box (large & readable for counter staff)
            sb.append("    <div style=\"text-align: center; background: #fff7ed; border: 2px dashed #f97316; padding: 6px 16px; border-radius: 12px; min-width: 120px;\">\n");
            sb.append("      <div style=\"font-size: 0.7rem; font-weight: 700; color: var(--primary); text-transform: uppercase;\">Pickup Token</div>\n");
            sb.append("      <div style=\"font-size: 1.8rem; font-weight: 900; color: #c2410c; letter-spacing: 1px; line-height: 1.1;\">").append(order.getPickupToken()).append("</div>\n");
            sb.append("    </div>\n");
            sb.append("  </div>\n");

            // Mobile-Adaptive Progress Stepper Bar
            if (order.getStatus() != OrderStatus.CANCELLED) {
                sb.append("  <div style=\"margin: 20px 0 16px;\">\n");
                sb.append("    <div style=\"display: flex; justify-content: space-between; position: relative;\">\n");

                // Background track line
                sb.append("      <div style=\"position: absolute; top: 15px; left: 8%; right: 8%; height: 3px; background: #e2e8f0; z-index: 1;\"></div>\n");

                int currentStep = order.getStatus().getStep();
                double progressPercent = (currentStep - 1) * 33.33;
                sb.append("      <div style=\"position: absolute; top: 15px; left: 8%; width: ").append(progressPercent).append("%; height: 3px; background: #10b981; z-index: 1; transition: width 0.4s ease;\"></div>\n");

                // Steps: 1 Placed, 2 Preparing, 3 Ready, 4 Completed
                String[][] steps = {
                    {"1", "Placed", "📝"},
                    {"2", "Kitchen", "👨‍🍳"},
                    {"3", "Ready", "🔔"},
                    {"4", "Picked Up", "✅"}
                };

                for (int i = 0; i < steps.length; i++) {
                    int stepNum = i + 1;
                    boolean isDone = stepNum <= currentStep;
                    boolean isCurrent = stepNum == currentStep;

                    sb.append("      <div style=\"position: relative; z-index: 2; text-align: center; flex: 1; max-width: 75px;\">\n");
                    sb.append("        <div style=\"width: 32px; height: 32px; border-radius: 50%; margin: 0 auto; display: flex; align-items: center; justify-content: center; font-size: 0.95rem; background: ")
                      .append(isDone ? "#10b981" : "#ffffff")
                      .append("; color: ").append(isDone ? "#ffffff" : "#94a3b8")
                      .append("; border: 2px solid ").append(isDone ? "#10b981" : "#cbd5e1")
                      .append("; box-shadow: ").append(isCurrent ? "0 0 0 3px #d1fae5" : "none")
                      .append(";\">")
                      .append(steps[i][2]).append("</div>\n");
                    sb.append("        <div style=\"font-size: 0.72rem; margin-top: 5px; font-weight: ").append(isCurrent ? "800" : "500")
                      .append("; color: ").append(isCurrent ? "var(--secondary)" : "var(--text-muted)")
                      .append("; white-space: nowrap;\">").append(steps[i][1]).append("</div>\n");
                    sb.append("      </div>\n");
                }

                sb.append("    </div>\n");
                sb.append("  </div>\n");
            }

            // Notification Banner for Ready Status
            if (order.getStatus() == OrderStatus.READY) {
                sb.append("  <div style=\"background: #d1fae5; border: 1px solid #6ee7b7; padding: 12px 14px; border-radius: 10px; margin: 14px 0; display: flex; align-items: center; gap: 10px;\">\n");
                sb.append("    <span style=\"font-size: 1.5rem;\">🔔</span>\n");
                sb.append("    <div>\n");
                sb.append("      <strong style=\"color: #065f46; font-size: 0.95rem;\">Food is ready for collection!</strong>\n");
                sb.append("      <p style=\"font-size: 0.8rem; color: #047857; margin: 0;\">Show Token <strong>").append(order.getPickupToken()).append("</strong> at Counter 1 to collect.</p>\n");
                sb.append("    </div>\n");
                sb.append("  </div>\n");
            }

            // Order Items Table / List
            sb.append("  <div style=\"margin-top: 14px; background: #f8fafc; border-radius: 8px; padding: 12px;\">\n");
            sb.append("    <div style=\"font-size: 0.75rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; margin-bottom: 6px;\">Items (").append(order.getTotalItemCount()).append(")</div>\n");
            sb.append("    <div style=\"display: flex; flex-direction: column; gap: 6px;\">\n");
            for (OrderItem itm : order.getItems()) {
                sb.append("      <div style=\"display: flex; justify-content: space-between; font-size: 0.88rem; padding: 3px 0; border-bottom: 1px dashed var(--border-color);\">\n");
                sb.append("        <span>").append(itm.getEmoji()).append(" ").append(escape(itm.getFoodName())).append(" <span style=\"color: var(--text-muted);\">×").append(itm.getQuantity()).append("</span></span>\n");
                sb.append("        <span style=\"font-weight: 700;\">").append(itm.getFormattedSubtotal()).append("</span>\n");
                sb.append("      </div>\n");
            }
            sb.append("    </div>\n");

            if (order.getNotes() != null && !order.getNotes().trim().isEmpty()) {
                sb.append("    <div style=\"margin-top: 6px; font-size: 0.78rem; color: var(--text-muted);\">\n");
                sb.append("      📝 <strong>Note:</strong> ").append(escape(order.getNotes())).append("\n");
                sb.append("    </div>\n");
            }

            sb.append("    <div style=\"display: flex; justify-content: space-between; align-items: baseline; margin-top: 8px; padding-top: 6px; border-top: 1px solid var(--border-color);\">\n");
            sb.append("      <span style=\"font-weight: 600; font-size: 0.85rem; color: var(--text-muted);\">Grand Total:</span>\n");
            sb.append("      <span style=\"font-size: 1.2rem; font-weight: 800; color: var(--primary);\">").append(order.getFormattedTotal()).append("</span>\n");
            sb.append("    </div>\n");
            sb.append("  </div>\n");

            sb.append("</div>\n");
        }

        sb.append("</div>\n");

        // Polling script
        sb.append("<script>\n");
        sb.append("  setTimeout(function() {\n");
        sb.append("    window.location.reload();\n");
        sb.append("  }, 7000);\n");
        sb.append("</script>\n");

        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
