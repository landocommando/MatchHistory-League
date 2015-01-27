package com.lando.matchhistory.Models;

import io.realm.RealmObject;

public class Item extends RealmObject{

    private long id;
    private String description;
    private String name;
    private Image image;
    private String group;
    public Item(){}

    public Item(long id, String description, String name, Image image, String group) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.image = image;
        this.group = group;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}