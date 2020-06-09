package com.example;

import com.google.gson.JsonParser;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

public class Controller {

    // todo: Add a loader to prevent multiple unnecessary HTTP requests?
    private static boolean auth = false; // todo: debugging
    private static final View view = View.getInstance();
    private static final String authUrl = "https://accounts.spotify.com/authorize?client_id=6edb9b1ac21042abacc6daaf0fbc4c4d"
            + "&redirect_uri=http://localhost:8080&response_type=code";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";


    public static void mainMenu() {
        System.out.println("Welcome to " + ANSI_BLUE + "Music Advisor" + ANSI_RESET + ".\nPlease enter a command:");
        Scanner sc = new Scanner(System.in);
        String input;
        String[] inSplit;
        String playlistType;

        input = sc.nextLine();
        do {
            playlistType = ""; // fixme: fix playlist from previous request being available to "playlists" input
            if ((inSplit = input.split(" ")).length > 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < inSplit.length; i++) { // Multiple word playlists
                    if (i == inSplit.length - 1) {
                        sb.append(inSplit[i]);
                    } else {
                        sb.append(inSplit[i]).append(" ");
                    }
                    playlistType = sb.toString();
                }
//                System.out.println("inSplit playlistType= " + playlistType);
                input = inSplit[0];
            }
            switch (input) {
                case "auth":
                    auth();
                    break;
                case "new":
                    newMusic();
                    break;
                case "featured":
                    featuredMusic();
                    break;
                case "categories":
                    categoriesMusic();
                    break;
                case "playlists":
                    playlistsMusic(playlistType);
                    break;
                case "next":
                    view.next();
                    break;
                case "prev":
                    view.prev();
                    break;
                case "exit":
                    System.out.println("---GOODBYE!---");
                    MusicGUI.exit();
//                    HttpUtils.stopHttpServer();
                    break;
                default:
                    System.out.println("Unknown command. Try again.");
            }
            input = sc.nextLine();
        } while (!input.equals("exit")); // fixme exit waits for GUI to close. Close GUI here!
    }

    private static void auth() {
        if (auth) {
            System.out.println("Already authenticated.");
            return;
        }
        System.out.println("Opening authentication website...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Desktop.getDesktop().browse(URI.create(authUrl));
        } catch (IOException e) {
            System.out.println("Failed to open browser automatically.\nUse this link to request the access code:");
            System.out.println(authUrl);
        }
        HttpUtils.startHttpServer();
        System.out.println("waiting for code...");
        HttpUtils.waitForCode();
        System.out.println("code received");
        System.out.println("making http request for access_token...\nresponse:");
        String status = "---FAILED---";
        if (HttpUtils.getAccessToken()) {
            auth = true;
            status = "---SUCCESS---";
        }
        System.out.println(status);
        System.out.println("Welcome " + JsonParser.parseString(HttpUtils.getFromApi("user",null))
                            .getAsJsonObject().get("display_name").getAsString().replaceAll("\"", "") + "!");
        } //todo: add this to model and say goodbye to <user>

    private static boolean checkAuth() {
        if (!auth) {
            System.out.println("Please, provide access for application.");
        }
        return auth;
    }

    private static void newMusic() {
        if (!checkAuth()) {
            return;
        }
        view.setContent(Model.getNew());
        view.update();
    }

    private static void featuredMusic() {
        if (!checkAuth()) {
            return;
        }
        view.setContent(Model.getFeatured());
        view.update();
    }

    private static void categoriesMusic() {
        if (!checkAuth()) {
            return;
        }
        view.setContent(Model.getCategories());
        view.update();
    }

    private static void playlistsMusic(String playlist) {
        if (!checkAuth()) {
            return;
        }
        view.setContent(Model.getPlaylists(playlist));
        view.update();
    }


}
