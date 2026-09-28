package com.canteen.view;

public class AuthView {

    public static String renderLogin(String errorMessage, String prefilledUsername) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\"max-width: 480px; margin: 30px auto;\">\n");
        sb.append("  <div class=\"card\" style=\"box-shadow: var(--shadow-lg);\">\n");
        sb.append("    <div style=\"text-align: center; margin-bottom: 24px;\">\n");
        sb.append("      <span style=\"font-size: 3rem; display: inline-block; margin-bottom: 8px;\">🎓</span>\n");
        sb.append("      <h2 style=\"font-size: 1.5rem; font-weight: 700; color: var(--secondary);\">Campus Canteen Portal</h2>\n");
        sb.append("      <p style=\"color: var(--text-muted); font-size: 0.9rem;\">Sign in to order food, track orders, or manage canteen operations</p>\n");
        sb.append("    </div>\n");

        if (errorMessage != null && !errorMessage.isEmpty()) {
            sb.append("    <div class=\"alert alert-error\" style=\"margin-bottom: 18px;\">").append(escape(errorMessage)).append("</div>\n");
        }

        sb.append("    <form method=\"POST\" action=\"/login\">\n");
        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\" for=\"username\">Username or Roll Number</label>\n");
        sb.append("        <input type=\"text\" id=\"username\" name=\"username\" class=\"form-control\" placeholder=\"e.g. student or 2024CS101\" required value=\"").append(escape(prefilledUsername != null ? prefilledUsername : "")).append("\">\n");
        sb.append("      </div>\n");

        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\" for=\"password\">Password</label>\n");
        sb.append("        <input type=\"password\" id=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Enter your password\" required>\n");
        sb.append("      </div>\n");

        sb.append("      <button type=\"submit\" class=\"btn btn-primary btn-block\" style=\"margin-top: 10px; font-size: 1rem; padding: 12px;\">Sign In</button>\n");
        sb.append("    </form>\n");

        // Demo Accounts Helper Box
        sb.append("    <div style=\"margin-top: 24px; padding: 16px; background: #f8fafc; border: 1px dashed var(--border-color); border-radius: 8px;\">\n");
        sb.append("      <div style=\"font-size: 0.8rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; margin-bottom: 8px;\">Quick Demo Logins</div>\n");
        sb.append("      <div style=\"display: grid; grid-template-columns: 1fr 1fr; gap: 8px;\">\n");
        sb.append("        <button type=\"button\" onclick=\"fillCredentials('student', 'student123')\" class=\"btn btn-outline btn-sm\" style=\"font-size: 0.8rem;\">🎓 Student Demo</button>\n");
        sb.append("        <button type=\"button\" onclick=\"fillCredentials('admin', 'admin123')\" class=\"btn btn-outline btn-sm\" style=\"font-size: 0.8rem;\">👨‍🍳 Staff Demo</button>\n");
        sb.append("      </div>\n");
        sb.append("    </div>\n");

        sb.append("    <div style=\"text-align: center; margin-top: 20px; font-size: 0.9rem; color: var(--text-muted);\">\n");
        sb.append("      New student here? <a href=\"/register\" style=\"color: var(--primary); font-weight: 600; text-decoration: none;\">Create an account</a>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        // Simple autofill helper script
        sb.append("<script>\n");
        sb.append("function fillCredentials(u, p) {\n");
        sb.append("  document.getElementById('username').value = u;\n");
        sb.append("  document.getElementById('password').value = p;\n");
        sb.append("}\n");
        sb.append("</script>\n");

        return sb.toString();
    }

    public static String renderRegister(String errorMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\"max-width: 520px; margin: 20px auto;\">\n");
        sb.append("  <div class=\"card\" style=\"box-shadow: var(--shadow-lg);\">\n");
        sb.append("    <div style=\"text-align: center; margin-bottom: 20px;\">\n");
        sb.append("      <span style=\"font-size: 2.5rem; display: inline-block; margin-bottom: 8px;\">📝</span>\n");
        sb.append("      <h2 style=\"font-size: 1.5rem; font-weight: 700; color: var(--secondary);\">Student Registration</h2>\n");
        sb.append("      <p style=\"color: var(--text-muted); font-size: 0.9rem;\">Register your college ID to order from the canteen online</p>\n");
        sb.append("    </div>\n");

        if (errorMessage != null && !errorMessage.isEmpty()) {
            sb.append("    <div class=\"alert alert-error\" style=\"margin-bottom: 16px;\">").append(escape(errorMessage)).append("</div>\n");
        }

        sb.append("    <form method=\"POST\" action=\"/register\">\n");
        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\" for=\"fullName\">Full Name *</label>\n");
        sb.append("        <input type=\"text\" id=\"fullName\" name=\"fullName\" class=\"form-control\" placeholder=\"e.g. Rohan Sharma\" required>\n");
        sb.append("      </div>\n");

        sb.append("      <div class=\"grid-responsive-2\" style=\"display: grid; grid-template-columns: 1fr 1fr; gap: 12px;\">\n");
        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label class=\"form-label\" for=\"rollNumber\">College Roll / ID *</label>\n");
        sb.append("          <input type=\"text\" id=\"rollNumber\" name=\"rollNumber\" class=\"form-control\" placeholder=\"e.g. 2024CS102\" required>\n");
        sb.append("        </div>\n");
        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label class=\"form-label\" for=\"phone\">Phone Number</label>\n");
        sb.append("          <input type=\"tel\" id=\"phone\" name=\"phone\" class=\"form-control\" placeholder=\"e.g. 9876543210\">\n");
        sb.append("        </div>\n");
        sb.append("      </div>\n");

        sb.append("      <div class=\"form-group\">\n");
        sb.append("        <label class=\"form-label\" for=\"email\">College Email</label>\n");
        sb.append("        <input type=\"email\" id=\"email\" name=\"email\" class=\"form-control\" placeholder=\"rohan@college.edu\">\n");
        sb.append("      </div>\n");

        sb.append("      <div class=\"grid-responsive-2\" style=\"display: grid; grid-template-columns: 1fr 1fr; gap: 12px;\">\n");
        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label class=\"form-label\" for=\"regUsername\">Username *</label>\n");
        sb.append("          <input type=\"text\" id=\"regUsername\" name=\"username\" class=\"form-control\" placeholder=\"Choose username\" required>\n");
        sb.append("        </div>\n");
        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label class=\"form-label\" for=\"regPassword\">Password *</label>\n");
        sb.append("          <input type=\"password\" id=\"regPassword\" name=\"password\" class=\"form-control\" placeholder=\"Min 4 chars\" required>\n");
        sb.append("        </div>\n");
        sb.append("      </div>\n");

        sb.append("      <button type=\"submit\" class=\"btn btn-primary btn-block\" style=\"margin-top: 14px; font-size: 1rem; padding: 12px;\">Create Student Account</button>\n");
        sb.append("    </form>\n");

        sb.append("    <div style=\"text-align: center; margin-top: 18px; font-size: 0.9rem; color: var(--text-muted);\">\n");
        sb.append("      Already registered? <a href=\"/login\" style=\"color: var(--primary); font-weight: 600; text-decoration: none;\">Sign In here</a>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");
        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
