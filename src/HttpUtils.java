import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpUtils {
    private static HttpServer server;
    private static final String CLIENT_ID = "6edb9b1ac21042abacc6daaf0fbc4c4d";
    private static final String CLIENT_SECRET = "secretKey";
    private static final String REDIRECT_ID = "http://localhost:8080";

    public static String uri;
    private static String query;
    private static String spotifyCode;
    private static String token;

    public static void startHttpServer() {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.start();
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    query = exchange.getRequestURI().getQuery();
                    String status;
                    if (query != null && query.contains("code=")) { //todo query .equeals("code=")
                        status = "Got the code. Return back to your program.";
                    } else {
                        status = "Not found authorization code. Try again.";
                    }
                    exchange.sendResponseHeaders(200, status.length());
                    exchange.getResponseBody().write(status.getBytes());
                    exchange.getResponseBody().close();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void waitForCode() {
        while (query == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        spotifyCode = query.substring(5);
        server.stop(1);
    }

    public static boolean getToken() { // to get auth token?
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-type", "application/x-www-form-urlencoded")
                    .uri(URI.create(uri + "/api/token"))
                    .POST(HttpRequest.BodyPublishers.ofString("&client_id=" + CLIENT_ID
                            + "&client_secret=" + CLIENT_SECRET
                            + "&grant_type=authorization_code"
                            + "&code=" + spotifyCode
                            + "&redirect_uri=" + REDIRECT_ID))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            token = response.body(); // todo: status 200 "success" else failed
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
