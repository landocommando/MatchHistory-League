package com.lando.matchhistory.Models;

import io.realm.RealmObject;

/**
 * Created by Lando on 12/2/2014.
 */


public class Champion extends RealmObject{

    private long id;
    private Image image;
    private String title;
    private String name;
    private String key;
    public Champion(){}

    public Champion(long id, Image image, String title, String name, String key) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.name = name;
        this.key = key;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
