package com.example.audiovideoplayer;

import java.io.Serializable;

public class Audios implements Serializable {

    String name, artist;
    long id;

    public Audios() {
        this.name = null;
        this.artist = null;
        this.id = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
