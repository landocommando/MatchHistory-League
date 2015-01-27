package com.lando.matchhistory.Models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Participant extends RealmObject{

    private RealmList<Mastery> masteries;
    private Stats stats;
    private RealmList<Rune> runes;
    private Timeline timeline;
    private int spell2Id;
    private int participantId;
    private long championId;
    private int teamId;
    private int spell1Id;


    public RealmList<Mastery> getMasteries() {
        return masteries;
    }

    public void setMasteries(RealmList<Mastery> masteries) {
        this.masteries = masteries;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public RealmList<Rune> getRunes() {
        return runes;
    }

    public void setRunes(RealmList<Rune> runes) {
        this.runes = runes;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public int getSpell2Id() {
        return spell2Id;
    }

    public void setSpell2Id(int spell2Id) {
        this.spell2Id = spell2Id;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public long getChampionId() {
        return championId;
    }

    public void setChampionId(long championId) {
        this.championId = championId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getSpell1Id() {
        return spell1Id;
    }

    public void setSpell1Id(int spell1Id) {
        this.spell1Id = spell1Id;
    }
}