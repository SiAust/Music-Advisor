package com.example;

import java.util.List;

public class SpotifyCollection<T extends SpotifyObject> {

    List<T> spotifyCollection;

    public SpotifyCollection(List<T> spotifyCollection) {
        this.spotifyCollection = spotifyCollection;
    }

    public void printItems() {
        for (T object : spotifyCollection) {
            System.out.println(object.toString());
        }
    }

    public int contains(String name) {
        for (int i = 0; i < spotifyCollection.size(); i++) {
            if (spotifyCollection.get(i).getName().toLowerCase().equals(name.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }
    public T get(int index) {
        return spotifyCollection.get(index);
    }
}

class SpotifyObject {
    String name;

    public SpotifyObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class SpotifyAlbum extends SpotifyObject {
    private List<String> artists;
    private String spotifyUrl;


    public SpotifyAlbum(String name, List<String> artists, String spotifyUrl) {
        super(name);
        this.artists = artists;
        this.spotifyUrl = spotifyUrl;
    }

    @Override
    public String toString() {
        return super.getName() + "\n" + artists + "\n" + spotifyUrl + "\n";
    }
}

class SpotifyCategories extends SpotifyObject {
    private String id;

    public SpotifyCategories(String name, String id) {
        super(name);
        this.id = id;
    }

    @Override
    public String toString() {
        return super.getName();
    }

    public String getId() {
        return id;
    }
}

class SpotifyFeatured extends SpotifyObject {
    private String url;

    public SpotifyFeatured(String name, String url) {
        super(name);
        this.url = url;
    }

    @Override
    public String toString() {
        return super.getName() + "\n" + url + "\n";
    }
}

class SpotifyPlaylists extends SpotifyObject {
    private String url;

    public SpotifyPlaylists(String name, String url) {
        super(name);
        this.url = url;
    }

    @Override
    public String toString() {
        return super.getName() + "\n" + url + "\n";
    }
}