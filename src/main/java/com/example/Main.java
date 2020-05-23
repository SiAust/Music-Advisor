package com.example;

import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    // todo: Add a loader to prevent multiple unnecessary HTTP requests?
    private static boolean auth = false; // todo: debugging
    private static SpotifyCollection<SpotifyObject> albums;
    private static SpotifyCollection<SpotifyObject> categories;
    private static SpotifyCollection<SpotifyObject> featured;
    private static SpotifyCollection<SpotifyObject> playlists;

    Logger logger = Logger.getLogger(Main.class.getName()); // todo: worth doing?

    public static void main(String[] args) {
        /*for (String string : args) {
            System.out.println(string);
        }*/
        if (args.length > 1) {
            if (args[0].contains("-access")) {
                HttpUtils.setAccessUri(args[1]);
            }
            if (args.length > 2) {
                if (args[2].contains("-resource")) {
                    HttpUtils.setResourceUri(args[3]);
                }
            }
        }
        play();
    }

    private static void play() {
        Scanner sc = new Scanner(System.in);
        String input;
        String[] inSplit;
        String playlistType;

        input = sc.nextLine();
        do {
            inSplit = input.split(" ");
            playlistType = "";
            if (inSplit.length > 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < inSplit.length; i++) { // multi word playlists
                    if (i == inSplit.length - 1) {
                        sb.append(inSplit[i]);
                    } else {
                        sb.append(inSplit[i]).append(" ");
                    }
                    playlistType = sb.toString();
                }
                System.out.println("inSplit playlistType= " + playlistType);
                input = inSplit[0];
            }
            switch (input) {
                case "auth":
                    auth();
                    input = sc.nextLine();
                    break;
                case "new":
                    newMusic();
                    input = sc.nextLine();
                    break;
                case "featured":
                    featuredMusic();
                    input = sc.nextLine();
                    break;
                case "categories":
                    categoriesMusic();
                    input = sc.nextLine();
                    break;
                case "playlists":
                    playlistsMusic(playlistType); // todo: more than one word in playlist? Needed? Or is just category name single word?
                    input = sc.nextLine();
                    break;
                case "exit":
//                    HttpUtils.stopHttpServer();
                    break;
                default:
                    System.out.println("Unknown command. Try again.");
                    input = sc.nextLine();
            }

        } while (!input.equals("exit"));
        System.out.println("---GOODBYE!---");
    }

    private static void auth() {
        System.out.println("Use this link to request the access code:");
        System.out.println("https://accounts.spotify.com/authorize?client_id=6edb9b1ac21042abacc6daaf0fbc4c4d"
                + "&redirect_uri=http://localhost:8080&response_type=code");
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
    }

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
        /*SpotifyCollection<SpotifyObject>*/
        albums = JSONUtils.getAlbumsFromJson(HttpUtils.getFromApi("new", null));
        albums.printItems();
    }

    private static void featuredMusic() {
        if (!checkAuth()) {
            return;
        }
        /* SpotifyCollection<SpotifyObject>*/
        featured = JSONUtils.getFeaturedFromJson(HttpUtils.getFromApi("featured", null));
        featured.printItems();
//        System.out.println(HttpUtils.getFromApi("featured"));
    }

    private static void categoriesMusic() {
        if (!checkAuth()) {
            return;
        }
        /*SpotifyCollection<SpotifyObject>*/
        categories = JSONUtils.getCategoriesFromJson(HttpUtils.getFromApi("categories", null));
        categories.printItems();
//        System.out.println(HttpUtils.getFromApi("categories"));
    }

    private static void playlistsMusic(String pList) {
//        System.out.println("playListsMusic pList= " + pList);
        if (!checkAuth()) {
            return;
        }
        if (categories == null) {
            categories = JSONUtils.getCategoriesFromJson(HttpUtils.getFromApi("categories", null));
        }
//        System.out.println("contains sleep=" + categories.contains("Sleep"));
        String playlist = "";
        int index;
        if ((index = categories.contains(pList)) >= 0) { //todo: something weird going on (no mood playlist? no sleep?
            SpotifyCategories object = (SpotifyCategories) categories.get(index);
            playlist = object.getId();
//            System.out.println("playlist from object= " + playlist); // show what the object id is
        }
//        System.out.println(HttpUtils.getFromApi("playlists"));
        playlists = JSONUtils.getPlaylistsFromJson(HttpUtils.getFromApi("playlists", "/" + playlist));
        if (playlists != null) {
            playlists.printItems();
        }
    }
}
