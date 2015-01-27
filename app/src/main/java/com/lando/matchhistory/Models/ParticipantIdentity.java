package com.lando.matchhistory.Models;

import io.realm.RealmObject;

public class ParticipantIdentity extends RealmObject{

    private Player player;
    private int participantId;


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }
}