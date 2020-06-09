package com.example;

import java.util.List;

public class SpotifyCollection<T extends SpotifyObject> {

    List<T> spotifyCollection;

    public SpotifyCollection(List<T> spotifyCollection) {
        this.spotifyCollection = spotifyCollection;
    }

    /**
     * This method prints all SpotifyObjects in a SpotifyCollection to to standard output.
     * @param n      Print n number of entries
     * @param pos    Start position of index
     */
    public void printItems(int n, int pos) {
        if (pos + n > spotifyCollection.size() - 1) { // 30 + 10 > 36 == true
            n -= (pos + n) - (spotifyCollection.size() - 1); // 40 - 35 = 5. n -5 = 5
        }
        for (int i = pos; i < pos + n; i++) { // todo: print n number of items, starting from position pos
            System.out.println(spotifyCollection.get(i).toString());
        }
    }

    // Temp method to display content in JFrame JLabel
    public String printItems() {
        StringBuilder sb = new StringBuilder();
        for (SpotifyObject object : spotifyCollection) {
            sb.append(object.toString());
        }
        return sb.toString();
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

    public int size() {
        return spotifyCollection.size();
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
        return super.getName() + "\n" + artists + "\n" + spotifyUrl + "\n\n";
    }
} // fixme: double "\n" to space out objects in JTextArea

class SpotifyCategories extends SpotifyObject {
    private String id;

    public SpotifyCategories(String name, String id) {
        super(name);
        this.id = id;
    }

    @Override
    public String toString() {
        return super.getName() + "\n";
    } // fixme: "\n" to create newline in JTextArea

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
        return super.getName() + "\n" + url + "\n\n";
    } // fixme: double "\n" to space out objects in JTextArea
}

class SpotifyPlaylists extends SpotifyObject {
    private String url;

    public SpotifyPlaylists(String name, String url) {
        super(name);
        this.url = url;
    }

    @Override
    public String toString() {
        return super.getName() + "\n" + url + "\n\n";
    } // fixme: double "\n" to space out objects in JTextArea
}