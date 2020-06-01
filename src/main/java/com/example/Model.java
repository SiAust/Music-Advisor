package com.example;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static SpotifyCollection<SpotifyObject> albums;
    private static SpotifyCollection<SpotifyObject> categories;
    private static SpotifyCollection<SpotifyObject> featured;
    private static SpotifyCollection<SpotifyObject> playlists;


    public static SpotifyCollection<SpotifyObject> getAlbums() {
        String json = HttpUtils.getFromApi("new", null);
        if (json == null) {
            return null;
        }
        JsonArray items = JsonParser.parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("albums")
                .getAsJsonArray("items");

        String name = null;
        List<String> artistsList = null;
        String spotifyUrl = null;
        List<SpotifyObject> albums = new ArrayList<>();

        for (JsonElement item : items) {
            // Get the album name
            name = item.getAsJsonObject().get("name").getAsString();
//            System.out.println(album.get("name"));

            // Get artist(s) from the JsonArray object
            JsonArray artists = item.getAsJsonObject().getAsJsonArray("artists");
            artistsList = new ArrayList<>(); // new object to prevent reusing the same reference?
            for (JsonElement artistElement : artists) {
                JsonObject artist = artistElement.getAsJsonObject();
//                System.out.println(artist.get("name"));
                artistsList.add(artist.get("name").getAsString());
            }

            // Get the external Spotify URL for the album
//            System.out.println(externalUrl.get("spotify"));
            spotifyUrl = item.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString();

            // Create a SpotifyAlbum object for each album and add to List of SpotifyObjects
            SpotifyObject spotifyAlbum = new SpotifyAlbum(name, artistsList, spotifyUrl);
//            System.out.println(spotifyAlbum.toString());
            albums.add(spotifyAlbum);

        }
        return new SpotifyCollection<>(albums);
    }

    public static SpotifyCollection<SpotifyObject> getCategories() {
//        System.out.println(json);
        String json = HttpUtils.getFromApi("categories", null);
        JsonArray items = JsonParser.parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("categories")
                .getAsJsonArray("items");

        String name = null;
        String id = null;
        List<SpotifyObject> categories = new ArrayList<>();
        for (JsonElement item : items) {
            name = item.getAsJsonObject().get("name").getAsString();
            id = item.getAsJsonObject().get("id").getAsString();
            SpotifyObject spotifyObject = new SpotifyCategories(name, id);
            categories.add(spotifyObject);
        }
        return new SpotifyCollection<>(categories);
    }

    public static SpotifyCollection<SpotifyObject> getFeatured() {
        String json = HttpUtils.getFromApi("featured", null);
        JsonArray items = JsonParser.parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("playlists")
                .getAsJsonArray("items");

        String url = "";
        String name = "";
        List<SpotifyObject> featured = new ArrayList<>();
        for (JsonElement item : items) {
            name = item.getAsJsonObject().get("name").getAsString();
            url = item.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString();
            featured.add(new SpotifyFeatured(name, url));
        }
        return new SpotifyCollection<>(featured);
    }

    public static SpotifyCollection<SpotifyObject> getPlaylists(String playlist) {
        if (categories == null) {
            categories = Model.getCategories();
        }
//        System.out.println("contains sleep=" + categories.contains("Sleep"));
        int index;
        if ((index = categories.contains(playlist)) >= 0) { //todo: something weird going on (no mood playlist? no sleep?
            SpotifyCategories object = (SpotifyCategories) categories.get(index);
            playlist = object.getId();
//            System.out.println("playlist from object= " + playlist); // show what the object id is
        }
        String json = HttpUtils.getFromApi("playlists", "/" + playlist);
        try {
            JsonArray items = JsonParser.parseString(json)
                    .getAsJsonObject()
                    .getAsJsonObject("playlists")
                    .getAsJsonArray("items");

            String name = "";
            String url = "";
            List<SpotifyObject> playlistsList = new ArrayList<>();
            for (JsonElement item : items) {
                name = item.getAsJsonObject().get("name").getAsString();
                url = item.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString();
                playlistsList.add(new SpotifyPlaylists(name, url));
            }
            return new SpotifyCollection<>(playlistsList);
        } catch (JsonSyntaxException | NullPointerException e) {
//            e.printStackTrace();
            System.out.println(JsonParser.parseString(json)
                    .getAsJsonObject()
                    .get("error")
                    .getAsJsonObject()
                    .get("message").getAsString());
            return null;
        }
    }
}
