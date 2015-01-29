package com.lando.matchhistory.Models;

import io.realm.RealmObject;

public class SummonerSpell extends RealmObject {

    private long id;
    private Image image;
    private String description;
    private String name;
    private String key;
    private long summonerLevel;
    public SummonerSpell(){}

    public SummonerSpell(long id, Image image, String description, String name, String key, long summonerLevel) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.name = name;
        this.key = key;
        this.summonerLevel = summonerLevel;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(long summonerLevel) {
        this.summonerLevel = summonerLevel;
    }
}