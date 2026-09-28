package test;

import com.canteen.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class HttpIntegrationTest {

    public static void main(String[] args) throws Exception {
        System.out.println("--- Starting HTTP Server on Port 8089 for Integration Testing ---");
        int testPort = 8089;

        // Start server in background thread
        Thread serverThread = new Thread(() -> {
            Main.main(new String[]{String.valueOf(testPort)});
        });
        serverThread.setDaemon(true);
        serverThread.start();

        // Give it a moment to bind
        Thread.sleep(800);

        // Test GET /
        testEndpoint("http://localhost:" + testPort + "/", 200, "CampusBite");

        // Test GET /menu
        testEndpoint("http://localhost:" + testPort + "/menu", 200, "All Items");

        // Test GET /menu with category filter
        testEndpoint("http://localhost:" + testPort + "/menu?category=BREAKFAST", 200, "Breakfast");

        // Test GET /login
        testEndpoint("http://localhost:" + testPort + "/login", 200, "Sign In");

        // Test GET /cart
        testEndpoint("http://localhost:" + testPort + "/cart", 200, "Your Food Cart");

        // Test GET /api/stats
        testEndpoint("http://localhost:" + testPort + "/api/stats", 200, "activeOrders");

        System.out.println("\nAll HTTP integration tests PASSED successfully! 🚀");
        System.exit(0);
    }

    private static void testEndpoint(String urlStr, int expectedStatus, String expectedText) throws Exception {
        System.out.print("Testing GET " + urlStr + " ... ");
        URL url = URI.create(urlStr).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);

        int code = conn.getResponseCode();
        assert code == expectedStatus : "Expected status " + expectedStatus + " but got " + code;

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        assert response.toString().contains(expectedText) : "Response did not contain expected text: '" + expectedText + "'";
        System.out.println("OK (" + code + ")");
    }
}
