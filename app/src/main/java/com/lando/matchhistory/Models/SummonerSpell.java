package com.lando.matchhistory.Models;


import io.realm.RealmObject;

public class SummonerSpell extends RealmObject{


    private long id;
    private String description;
    private String name;
    private Image image;
    private String key;
    private long summonerLevel;
    public SummonerSpell(){}
    public SummonerSpell(String description, String name, Image image, String key, Integer summonerLevel) {

        this.description = description;
        this.name = name;
        this.image = image;
        this.key = key;
        this.summonerLevel = summonerLevel;
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