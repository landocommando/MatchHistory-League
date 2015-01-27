package com.lando.matchhistory.Models;

/**
 * Created by Lando on 12/2/2014.
 */

import io.realm.RealmObject;

public class Champion extends RealmObject{

    private long id;
    private String title;
    private String name;
    private String key;
    private Image image;
    public Champion(){}

    public Champion(long id, String title, String name, String key, Image image) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.key = key;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
