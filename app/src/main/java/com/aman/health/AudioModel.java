package com.aman.health;


import java.io.Serializable;

public class AudioModel implements Serializable {
    String Album;
    String path;
    String title;
    String duration;
    String Artist;
    String AlbumArt;

    public AudioModel(String path, String title, String duration) {
        this.path = path;
        this.Album = Album;
        this.title = title;
        this.duration = duration;
        this.Artist = Artist;
        this.AlbumArt = AlbumArt;
    }
    public String getAlbum() { return Album; }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArtist(){return Artist;}

    public String getAlbumArt(){return AlbumArt;}
}
