package com.lando.matchhistory.Models;

import io.realm.RealmObject;

public class Item extends RealmObject {

    private long id;
    private Image image;
    private String description;
    private String name;
    private String group;
    public Item(){}

    public Item(long id, Image image, String description, String name, String group) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.name = name;
        this.group = group;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}