package com.example;

import java.util.ArrayList;
import java.util.List;

public class SpotifyDecorator {
    public static void main(String[] args) {
        // Featured
        MusicItem featured = new MusicUrl("www.spotify.com/music/grimes", new MusicTitle("Oblivion")  );
//        System.out.println(featured.makeMusic());

        // New music
        MusicItem newMusic = new MusicUrl("www.spotify.com/new", new MusicArtist(List.of("Foo Fighters", "QotSA"), new MusicTitle("One By One")));
        System.out.println(newMusic.makeMusic());

        // Categories
        MusicItem categories = new MusicTitle("Sleep");
        System.out.println(categories.makeMusic());

        // Playlists
        MusicItem playlists = new MusicUrl("www.spotify.com/playlists", new MusicTitle("Sleep"));
        System.out.println(playlists.makeMusic());

        List<MusicItem> list = new ArrayList<>();
        list.add(featured);
        list.add(playlists);
        list.add(newMusic);
        list.add(categories);
        SpotifyCollect spotifyCollect = new SpotifyCollect(list);
        spotifyCollect.print();


    }

}

class SpotifyCollect {
    List<MusicItem> items;

    public SpotifyCollect(List<MusicItem> items) {
        this.items = items;
    }

    public void print() {
        for (MusicItem item : items) {
            System.out.println(item.makeMusic());
            System.out.println("\n");

        }
    }
}

/* Music interface */
interface MusicItem {
    String makeMusic();
}

/* concrete MusicItem */
class MusicTitle implements MusicItem {
    private String title;

    public MusicTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String makeMusic() {
        return title;
    }
}


class MusicDecorator implements MusicItem {
    private MusicItem musicItem;

    public MusicDecorator(MusicItem musicItem) {
        this.musicItem = musicItem;
    }

    @Override
    public String makeMusic() {
       return musicItem.makeMusic();
    }
}

class MusicArtist extends MusicDecorator {
    private List<String> artists;

    public MusicArtist(List<String> artists, MusicItem musicItem) {
        super(musicItem);
        this.artists = artists;
    }

    @Override
    public String makeMusic() {
        return super.makeMusic() + "\n" + artists;
    }
}

class MusicUrl extends MusicDecorator {
    private String url;

    public MusicUrl(String url, MusicItem musicItem) {
        super(musicItem);
        this.url = url;
    }

    @Override
    public String makeMusic() {
        return super.makeMusic() + "\n" + url;
    }
}