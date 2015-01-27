package com.lando.matchhistory.Models;


import io.realm.RealmObject;

public class Rune extends RealmObject{

    private int rank;
    private long runeId;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getRuneId() {
        return runeId;
    }

    public void setRuneId(long runeId) {
        this.runeId = runeId;
    }
}