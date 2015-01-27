package com.lando.matchhistory.Models;


import io.realm.RealmObject;

public class Timeline extends RealmObject{

    private XpPerMinDeltas xpPerMinDeltas;
    private GoldPerMinDeltas goldPerMinDeltas;
    private String role;
    private CreepsPerMinDeltas creepsPerMinDeltas;
    private DamageTakenPerMinDeltas damageTakenPerMinDeltas;
    private String lane;

    public XpPerMinDeltas getXpPerMinDeltas() {
        return xpPerMinDeltas;
    }

    public void setXpPerMinDeltas(XpPerMinDeltas xpPerMinDeltas) {
        this.xpPerMinDeltas = xpPerMinDeltas;
    }

    public GoldPerMinDeltas getGoldPerMinDeltas() {
        return goldPerMinDeltas;
    }

    public void setGoldPerMinDeltas(GoldPerMinDeltas goldPerMinDeltas) {
        this.goldPerMinDeltas = goldPerMinDeltas;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public CreepsPerMinDeltas getCreepsPerMinDeltas() {
        return creepsPerMinDeltas;
    }

    public void setCreepsPerMinDeltas(CreepsPerMinDeltas creepsPerMinDeltas) {
        this.creepsPerMinDeltas = creepsPerMinDeltas;
    }

    public DamageTakenPerMinDeltas getDamageTakenPerMinDeltas() {
        return damageTakenPerMinDeltas;
    }

    public void setDamageTakenPerMinDeltas(DamageTakenPerMinDeltas damageTakenPerMinDeltas) {
        this.damageTakenPerMinDeltas = damageTakenPerMinDeltas;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }
}