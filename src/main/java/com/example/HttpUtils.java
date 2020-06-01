package com.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtils {

    private static HttpServer server;
    private static final String CLIENT_ID = "6edb9b1ac21042abacc6daaf0fbc4c4d";
    private static final String CLIENT_SECRET = Config.getSecret(); // todo: hash the string
    private static final String REDIRECT_ID = "http://localhost:8080";

    private static String accessUri = "https://accounts.spotify.com";
    private static String resourceUri = "https://api.spotify.com";
    private static String query;
    private static String spotifyCode;
    private static String accessToken;

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
                    if (query != null && query.contains("code=")) {
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

    public static void waitForCode() { // todo: refresh token state? set query null to prevent auth failure?
        while (query == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        spotifyCode = query.substring(5);
//        query = null; // so that we can auth again?
        server.stop(1);
    }

    public static boolean getAccessToken() { // to get auth token?
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-type", "application/x-www-form-urlencoded")
                    .uri(URI.create(accessUri + "/api/token"))
                    .POST(HttpRequest.BodyPublishers.ofString("&client_id=" + CLIENT_ID
                            + "&client_secret=" + CLIENT_SECRET
                            + "&grant_type=authorization_code"
                            + "&code=" + spotifyCode
                            + "&redirect_uri=" + REDIRECT_ID))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.body());
//            accessToken = response.body();
            if (response.statusCode() > 200) {
                return false;
                // todo: get status from here and check 200 else fail?
            }
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            accessToken = jsonResponse.get("access_token").getAsString();
//            System.out.println(accessToken);
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFromApi(String getType, String playlist) {
        String path = "";
        switch (getType) {
            case "new":
                path = "/v1/browse/new-releases";
                break;
            case "categories":
                path = "/v1/browse/categories?limit=45"; // todo: get complete list of categories.
                break;
            case "featured":
                path = "/v1/browse/featured-playlists";
                break;
            case "playlists":
                path = String.format("/v1/browse/categories%s/playlists", playlist);
        }
        String responseJson = "";
        try {
//            startHttpServer();
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .uri(URI.create(resourceUri + path))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.body());
            responseJson = response.body();

         /*   JsonObject limitJson = JsonParser.parseString(responseJson).getAsJsonObject().getAsJsonObject("categories");
            String limit = limitJson.get("limit").getAsString();
            String total = limitJson.get("total").getAsString();
            System.out.println("limit= " + limit + " total= " + total);*/

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
//        server.stop(1);
        return responseJson;
    }

    public static void setAccessUri(String accessUri) {
        HttpUtils.accessUri = accessUri;
    }

    public static void setResourceUri(String resourceUri) {
        HttpUtils.resourceUri = resourceUri;
    }
}
