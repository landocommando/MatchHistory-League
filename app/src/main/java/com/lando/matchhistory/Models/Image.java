package com.lando.matchhistory.Models;


import io.realm.RealmObject;

public class Image extends RealmObject{

    private int w;
    private String full;
    private String sprite;
    private int h;
    private int y;
    private int x;
    public Image(){}

    public Image(int w, String full, String sprite, int h, int y, int x) {
        this.w = w;
        this.full = full;
        this.sprite = sprite;
        this.h = h;
        this.y = y;
        this.x = x;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}