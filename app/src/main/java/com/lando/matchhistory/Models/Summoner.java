package com.lando.matchhistory.Models;

import io.realm.RealmList;
import io.realm.RealmObject;


public class Summoner extends RealmObject{

    private long id;
    private String name;
    private long profileIconId;
    private long revisionDate;
    private long summonerLevel;
    private RealmList<Match> matches;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(long profileIconId) {
        this.profileIconId = profileIconId;
    }

    public long getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(long revisionDate) {
        this.revisionDate = revisionDate;
    }

    public long getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(long summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public RealmList<Match> getMatches() {
        return matches;
    }

    public void setMatches(RealmList<Match> matches) {
        this.matches = matches;
    }
}