package com.example;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {


    public static SpotifyCollection<SpotifyObject> getAlbumsFromJson(String json) {
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
            JsonObject externalUrl = item.getAsJsonObject().getAsJsonObject("external_urls");
//            System.out.println(externalUrl.get("spotify"));
            spotifyUrl = externalUrl.get("spotify").getAsString();

            // Create a SpotifyAlbum object for each album and add to List of SpotifyObjects
            SpotifyObject spotifyAlbum = new SpotifyAlbum(name, artistsList, spotifyUrl);
//            System.out.println(spotifyAlbum.toString());
            albums.add(spotifyAlbum);

        }
        return new SpotifyCollection<>(albums);
    }

    public static SpotifyCollection<SpotifyObject> getCategoriesFromJson(String json) {
//        System.out.println(json);
        JsonArray items = JsonParser.parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("categories")
                .getAsJsonArray("items");

        String name = null;
        String id = null;
        List<SpotifyObject> categories = new ArrayList<>();
        for (JsonElement item : items) {
            JsonObject category = item.getAsJsonObject(); // todo: one-liner
            name = category.get("name").getAsString();
            id = category.get("id").getAsString();
            SpotifyObject spotifyObject = new SpotifyCategories(name, id);
            categories.add(spotifyObject);
        }

        return new SpotifyCollection<>(categories);
    }

    public static SpotifyCollection<SpotifyObject> getFeaturedFromJson(String json) {
        JsonArray items = JsonParser.parseString(json)
                .getAsJsonObject()
                .getAsJsonObject("playlists")
                .getAsJsonArray("items");

        String url = "";
        String name = "";
        List<SpotifyObject> featured = new ArrayList<>();
        for (JsonElement item : items) {
//            JsonObject featuredItem = item.getAsJsonObject(); // todo: can one-liner this
            name = item.getAsJsonObject().get("name").getAsString();
//            JsonObject externalUrls = featuredItem.getAsJsonObject("external_urls"); // todo: extend one-liner
            url = item.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString();
//            SpotifyObject spotifyObject = new SpotifyFeatured(name, url); // todo: extend one-liner
            featured.add(new SpotifyFeatured(name, url));

        }
        return new SpotifyCollection<>(featured);
    }

    public static SpotifyCollection<SpotifyObject> getPlaylistsFromJson(String json) {
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
