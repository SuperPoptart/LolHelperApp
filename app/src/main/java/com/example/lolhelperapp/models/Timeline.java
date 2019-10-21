package com.example.lolhelperapp.models;

import com.example.lolhelperapp.models.deltas.CreepsPerMinDeltas;
import com.example.lolhelperapp.models.deltas.CsDiffPerMinDeltas;
import com.example.lolhelperapp.models.deltas.DamageTakenDiffPerMinDeltas;
import com.example.lolhelperapp.models.deltas.DamageTakenPerMinDeltas;
import com.example.lolhelperapp.models.deltas.GoldPerMinDeltas;
import com.example.lolhelperapp.models.deltas.XpDiffPerMinDeltas;
import com.example.lolhelperapp.models.deltas.XpPerMinDeltas;

public class Timeline {
    private int participantId;
    private CreepsPerMinDeltas creepsPerMinDeltas;
    private XpPerMinDeltas xpPerMinDeltas;
    private GoldPerMinDeltas goldPerMinDeltas;
    private CsDiffPerMinDeltas csDiffPerMinDeltas;
    private XpDiffPerMinDeltas xpDiffPerMinDeltas;
    private DamageTakenPerMinDeltas damageTakenPerMinDeltas;
    private DamageTakenDiffPerMinDeltas damageTakenDiffPerMinDeltas;
    private String role;
    private String lane;

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public CreepsPerMinDeltas getCreepsPerMinDeltas() {
        return creepsPerMinDeltas;
    }

    public void setCreepsPerMinDeltas(CreepsPerMinDeltas creepsPerMinDeltas) {
        this.creepsPerMinDeltas = creepsPerMinDeltas;
    }

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

    public CsDiffPerMinDeltas getCsDiffPerMinDeltas() {
        return csDiffPerMinDeltas;
    }

    public void setCsDiffPerMinDeltas(CsDiffPerMinDeltas csDiffPerMinDeltas) {
        this.csDiffPerMinDeltas = csDiffPerMinDeltas;
    }

    public XpDiffPerMinDeltas getXpDiffPerMinDeltas() {
        return xpDiffPerMinDeltas;
    }

    public void setXpDiffPerMinDeltas(XpDiffPerMinDeltas xpDiffPerMinDeltas) {
        this.xpDiffPerMinDeltas = xpDiffPerMinDeltas;
    }

    public DamageTakenPerMinDeltas getDamageTakenPerMinDeltas() {
        return damageTakenPerMinDeltas;
    }

    public void setDamageTakenPerMinDeltas(DamageTakenPerMinDeltas damageTakenPerMinDeltas) {
        this.damageTakenPerMinDeltas = damageTakenPerMinDeltas;
    }

    public DamageTakenDiffPerMinDeltas getDamageTakenDiffPerMinDeltas() {
        return damageTakenDiffPerMinDeltas;
    }

    public void setDamageTakenDiffPerMinDeltas(DamageTakenDiffPerMinDeltas damageTakenDiffPerMinDeltas) {
        this.damageTakenDiffPerMinDeltas = damageTakenDiffPerMinDeltas;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }
}
