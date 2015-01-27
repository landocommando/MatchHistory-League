package com.lando.matchhistory.Models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Match extends RealmObject{

    private String matchVersion;
    private String region;
    private long mapId;
    private String season;
    private String queueType;
    private long matchDuration;
    private long matchCreation;
    private String matchType;
    private long matchId;
    private RealmList<Participant> participants;
    private String matchMode;
    private String platformId;
    private RealmList<ParticipantIdentity> participantIdentities;


    public String getMatchVersion() {
        return matchVersion;
    }

    public void setMatchVersion(String matchVersion) {
        this.matchVersion = matchVersion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public long getMatchDuration() {
        return matchDuration;
    }

    public void setMatchDuration(long matchDuration) {
        this.matchDuration = matchDuration;
    }

    public long getMatchCreation() {
        return matchCreation;
    }

    public void setMatchCreation(long matchCreation) {
        this.matchCreation = matchCreation;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public RealmList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(RealmList<Participant> participants) {
        this.participants = participants;
    }

    public String getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(String matchMode) {
        this.matchMode = matchMode;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public RealmList<ParticipantIdentity> getParticipantIdentities() {
        return participantIdentities;
    }

    public void setParticipantIdentities(RealmList<ParticipantIdentity> participantIdentities) {
        this.participantIdentities = participantIdentities;
    }
}