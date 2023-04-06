package com.example.audiovideoplayer;

public class Videos {

    String name;
    String url;
    long id;

    public Videos() {
        this.name = null;
        this.url = null;
        this.id = 0;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
