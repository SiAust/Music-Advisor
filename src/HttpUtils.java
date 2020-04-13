import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HttpUtils {
    private static HttpServer server;
    private static final String CLIENT_ID = "6edb9b1ac21042abacc6daaf0fbc4c4d";
    private static final String CLIENT_SECRET = ""; // todo: secret
    private static final String REDIRECT_ID = "http://localhost:8080";

    public static void startHttpServer() {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    String query = /*"hey buddy, this is a Java server, wouldn't you know"; */exchange.getRequestURI().getQuery();
                    exchange.sendResponseHeaders(200, query.length());
                    exchange.getResponseBody().write(query.getBytes());
                    exchange.getResponseBody().close();
                }
            });
            server.start();
            System.out.println("*** Started server ***");
            System.out.println("Use this link to request the access code:");
            System.out.println("https://accounts.spotify.com/authorize?client_id=6edb9b1ac21042abacc6daaf0fbc4c4d&redirect_uri=http://localhost:8080&response_type=code");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static void getResponse() { // get the response from the server?!
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.stop(1);
        }
    }*/

    public static CompletableFuture<String> get() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080"))
                .GET()
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    private static String clientRequest() { // to get auth token?
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-type", "application/x-www-form-urlencoded")
                    .uri(URI.create("https://accounts.spotify.com/authorize"))
                    .POST(HttpRequest.BodyPublishers.ofString("&client_id=" + CLIENT_ID
                            + "response_type=code"
                            + "&redirect_uri=" + REDIRECT_ID
                            + "&scope=user-read-private user-read-email"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Not found authorisation code. Try again.";
        }

    }
}
