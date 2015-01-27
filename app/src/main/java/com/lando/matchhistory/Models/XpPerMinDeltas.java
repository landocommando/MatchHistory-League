package com.lando.matchhistory.Models;


import io.realm.RealmObject;

public class XpPerMinDeltas extends RealmObject{

    private float zeroToTen;
    private float tenToTwenty;
    private float twentyToThirty;

    public float getZeroToTen() {
        return zeroToTen;
    }

    public void setZeroToTen(float zeroToTen) {
        this.zeroToTen = zeroToTen;
    }

    public float getTenToTwenty() {
        return tenToTwenty;
    }

    public void setTenToTwenty(float tenToTwenty) {
        this.tenToTwenty = tenToTwenty;
    }

    public float getTwentyToThirty() {
        return twentyToThirty;
    }

    public void setTwentyToThirty(float twentyToThirty) {
        this.twentyToThirty = twentyToThirty;
    }
}