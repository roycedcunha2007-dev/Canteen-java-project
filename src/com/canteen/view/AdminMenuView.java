package com.canteen.view;

import com.canteen.model.Category;
import com.canteen.model.FoodItem;

import java.util.List;

public class AdminMenuView {

    public static String render(List<FoodItem> items, FoodItem editItem) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div style=\"display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 10px;\">\n");
        sb.append("  <div>\n");
        sb.append("    <h1 style=\"font-size: 1.6rem; font-weight: 800; color: var(--secondary);\">📋 Menu Management</h1>\n");
        sb.append("    <p style=\"color: var(--text-muted); font-size: 0.88rem;\">Add delicacies, update prices, and toggle 1-click stock availability</p>\n");
        sb.append("  </div>\n");
        sb.append("  <div>\n");
        sb.append("    <a href=\"#addItemCard\" class=\"btn btn-primary btn-sm\" style=\"min-height: 38px;\">➕ Add Food Item</a>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        // Add or Edit Form
        boolean isEdit = editItem != null;
        sb.append("<div id=\"addItemCard\" class=\"card\" style=\"margin-bottom: 24px; border-top: 4px solid var(--primary); padding: 18px;\">\n");
        sb.append("  <div style=\"display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;\">\n");
        sb.append("    <h3 style=\"font-size: 1.15rem; font-weight: 700; color: var(--secondary);\">")
          .append(isEdit ? "✏️ Edit Food Item: " + escape(editItem.getName()) : "➕ Add New Canteen Food Item").append("</h3>\n");
        if (isEdit) {
            sb.append("    <a href=\"/admin/menu\" class=\"btn btn-outline btn-sm\">Cancel</a>\n");
        }
        sb.append("  </div>\n");

        sb.append("  <form method=\"POST\" action=\"").append(isEdit ? "/admin/menu/update" : "/admin/menu/add").append("\">\n");
        if (isEdit) {
            sb.append("    <input type=\"hidden\" name=\"id\" value=\"").append(editItem.getId()).append("\">\n");
        }

        sb.append("    <div class=\"grid-responsive-3\" style=\"display: grid; grid-template-columns: 2fr 1fr 1fr; gap: 12px;\">\n");

        // Item Name
        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\">Food Name *</label>\n");
        sb.append("        <input type=\"text\" name=\"name\" class=\"form-control\" placeholder=\"e.g. Pav Bhaji\" required value=\"")
          .append(isEdit ? escape(editItem.getName()) : "").append("\">\n");
        sb.append("      </div>\n");

        // Category
        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\">Category *</label>\n");
        sb.append("        <select name=\"category\" class=\"form-control\">\n");
        for (Category c : Category.values()) {
            if (c == Category.ALL) continue;
            boolean sel = isEdit && editItem.getCategory() == c;
            sb.append("          <option value=\"").append(c.name()).append("\" ").append(sel ? "selected" : "").append(">").append(c.getDisplayName()).append("</option>\n");
        }
        sb.append("        </select>\n");
        sb.append("      </div>\n");

        // Price
        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\">Price (₹) *</label>\n");
        sb.append("        <input type=\"number\" step=\"0.5\" name=\"price\" class=\"form-control\" placeholder=\"e.g. 50\" required value=\"")
          .append(isEdit ? String.format("%.2f", editItem.getPrice()) : "").append("\">\n");
        sb.append("      </div>\n");

        sb.append("    </div>\n");

        sb.append("    <div class=\"grid-responsive-3\" style=\"display: grid; grid-template-columns: 1fr 1fr 2fr; gap: 12px;\">\n");

        // Emoji
        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\">Icon / Emoji</label>\n");
        sb.append("        <input type=\"text\" name=\"emoji\" class=\"form-control\" placeholder=\"e.g. 🍛, 🥞\" value=\"")
          .append(isEdit ? escape(editItem.getEmoji()) : "🍲").append("\">\n");
        sb.append("      </div>\n");

        // Prep Time
        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\">Prep Time (Mins)</label>\n");
        sb.append("        <input type=\"number\" name=\"prepTimeMinutes\" class=\"form-control\" placeholder=\"e.g. 8\" value=\"")
          .append(isEdit ? editItem.getPrepTimeMinutes() : 8).append("\">\n");
        sb.append("      </div>\n");

        // Description
        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\">Description</label>\n");
        sb.append("        <input type=\"text\" name=\"description\" class=\"form-control\" placeholder=\"Sides, ingredients\" value=\"")
          .append(isEdit ? escape(editItem.getDescription()) : "").append("\">\n");
        sb.append("      </div>\n");

        sb.append("    </div>\n");

        sb.append("    <div style=\"display: flex; justify-content: flex-end; gap: 10px; margin-top: 8px;\">\n");
        sb.append("      <button type=\"submit\" class=\"btn btn-primary\" style=\"min-height: 44px; width: 100%; max-width: 250px;\">").append(isEdit ? "💾 Update Item" : "➕ Save & Publish").append("</button>\n");
        sb.append("    </div>\n");

        sb.append("  </form>\n");
        sb.append("</div>\n");

        // Table of Current Items with mobile horizontal scroll
        sb.append("<div class=\"card\" style=\"padding: 16px;\">\n");
        sb.append("  <div style=\"display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;\">\n");
        sb.append("    <h3 style=\"font-size: 1.15rem; font-weight: 700; color: var(--secondary);\">All Menu Items (").append(items.size()).append(")</h3>\n");
        sb.append("    <span style=\"font-size: 0.8rem; color: var(--text-muted);\">1-Tap Stock Toggle</span>\n");
        sb.append("  </div>\n");

        sb.append("  <div style=\"overflow-x: auto; -webkit-overflow-scrolling: touch;\">\n");
        sb.append("    <table style=\"width: 100%; min-width: 520px; border-collapse: collapse; font-size: 0.9rem;\">\n");
        sb.append("      <thead>\n");
        sb.append("        <tr style=\"border-bottom: 2px solid var(--border-color); text-align: left;\">\n");
        sb.append("          <th style=\"padding: 8px; color: var(--text-muted); font-size: 0.75rem; text-transform: uppercase;\">Food Item</th>\n");
        sb.append("          <th style=\"padding: 8px; color: var(--text-muted); font-size: 0.75rem; text-transform: uppercase;\">Category</th>\n");
        sb.append("          <th style=\"padding: 8px; color: var(--text-muted); font-size: 0.75rem; text-transform: uppercase;\">Price</th>\n");
        sb.append("          <th style=\"padding: 8px; color: var(--text-muted); font-size: 0.75rem; text-transform: uppercase; text-align: center;\">Stock</th>\n");
        sb.append("          <th style=\"padding: 8px; color: var(--text-muted); font-size: 0.75rem; text-transform: uppercase; text-align: right;\">Actions</th>\n");
        sb.append("        </tr>\n");
        sb.append("      </thead>\n");
        sb.append("      <tbody>\n");

        for (FoodItem item : items) {
            sb.append("        <tr style=\"border-bottom: 1px solid var(--border-color);\">\n");

            // Name & Emoji
            sb.append("          <td style=\"padding: 10px 8px;\">\n");
            sb.append("            <div style=\"display: flex; align-items: center; gap: 8px;\">\n");
            sb.append("              <span style=\"font-size: 1.6rem;\">").append(item.getEmoji()).append("</span>\n");
            sb.append("              <div>\n");
            sb.append("                <strong style=\"color: var(--secondary); font-size: 0.92rem;\">").append(escape(item.getName())).append("</strong>\n");
            sb.append("              </div>\n");
            sb.append("            </div>\n");
            sb.append("          </td>\n");

            // Category
            sb.append("          <td style=\"padding: 10px 8px;\"><span class=\"badge badge-info\" style=\"font-size: 0.7rem;\">").append(item.getCategory().getDisplayName()).append("</span></td>\n");

            // Price
            sb.append("          <td style=\"padding: 10px 8px; font-weight: 700; color: var(--secondary);\">").append(item.getFormattedPrice()).append("</td>\n");

            // Stock Status & 1-Click Toggle
            sb.append("          <td style=\"padding: 10px 8px; text-align: center;\">\n");
            sb.append("            <form method=\"POST\" action=\"/admin/menu/toggle\" style=\"display: inline;\">\n");
            sb.append("              <input type=\"hidden\" name=\"id\" value=\"").append(item.getId()).append("\">\n");
            if (item.isAvailable()) {
                sb.append("              <button type=\"submit\" class=\"btn btn-sm\" style=\"background: #d1fae5; color: #065f46; border: 1px solid #6ee7b7; cursor: pointer; min-height: 32px; font-size: 0.78rem; padding: 4px 8px;\" title=\"Click to mark Sold Out\">🟢 In Stock</button>\n");
            } else {
                sb.append("              <button type=\"submit\" class=\"btn btn-sm\" style=\"background: #fee2e2; color: #991b1b; border: 1px solid #fca5a5; cursor: pointer; min-height: 32px; font-size: 0.78rem; padding: 4px 8px;\" title=\"Click to mark In Stock\">🔴 Sold Out</button>\n");
            }
            sb.append("            </form>\n");
            sb.append("          </td>\n");

            // Edit & Delete Actions
            sb.append("          <td style=\"padding: 10px 8px; text-align: right;\">\n");
            sb.append("            <div style=\"display: inline-flex; gap: 4px;\">\n");
            sb.append("              <a href=\"/admin/menu?edit=").append(item.getId()).append("#addItemCard\" class=\"btn btn-outline btn-sm\" style=\"min-height: 32px; padding: 4px 8px; font-size: 0.78rem;\" title=\"Edit\">✏️</a>\n");
            sb.append("              <form method=\"POST\" action=\"/admin/menu/delete\" style=\"display: inline;\" onsubmit=\"return confirm('Delete this food item?');\">\n");
            sb.append("                <input type=\"hidden\" name=\"id\" value=\"").append(item.getId()).append("\">\n");
            sb.append("                <button type=\"submit\" class=\"btn btn-outline btn-sm\" style=\"color: #ef4444; border-color: #fecaca; min-height: 32px; padding: 4px 8px; font-size: 0.78rem;\" title=\"Delete\">🗑️</button>\n");
            sb.append("              </form>\n");
            sb.append("            </div>\n");
            sb.append("          </td>\n");

            sb.append("        </tr>\n");
        }

        sb.append("      </tbody>\n");
        sb.append("    </table>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
