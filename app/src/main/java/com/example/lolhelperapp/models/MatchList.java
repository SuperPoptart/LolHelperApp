package com.example.lolhelperapp.models;

import com.example.lolhelperapp.models.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchList {
    private List<Match> matches;

    public MatchList(){
        matches = new ArrayList<>();
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
