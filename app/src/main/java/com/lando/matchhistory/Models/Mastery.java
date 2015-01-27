package com.lando.matchhistory.Models;



import io.realm.RealmObject;

public class Mastery extends RealmObject{

    private int rank;
    private long masteryId;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getMasteryId() {
        return masteryId;
    }

    public void setMasteryId(long masteryId) {
        this.masteryId = masteryId;
    }
}