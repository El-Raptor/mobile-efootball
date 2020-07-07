package com.example.trabalhofinal.controller;

import android.content.Context;

import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.Match;
import com.example.trabalhofinal.data.model.Player;
import com.example.trabalhofinal.data.model.Team;
import com.example.trabalhofinal.data.model.User;

import java.util.List;

public class MatchController {

    private Context context;
    private DBHelper db;

    public MatchController(Context context) {
        this.context = context;
    }

    public void addMatch(Match newMatch, User loggedUser) {

        db = DBHelper.getInstance(context);

        TeamController tc = new TeamController(context);
        tc.checkTeam(tc.assembleTeam(newMatch,1), db, loggedUser, newMatch, true);
        tc.checkTeam(tc.assembleTeam(newMatch,2), db, loggedUser, newMatch, true);

        PlayerController pc = new PlayerController(context);
        pc.checkPlayer(pc.assemblePlayer(newMatch), db, loggedUser, newMatch, true);

        db.addMatch(newMatch, loggedUser);

        db.close();

    }

    public void updateMatch(Match oldMatch, Match newMatch, User loggedUser) {
        db = DBHelper.getInstance(context);

        newMatch.setId(oldMatch.getId());

        TeamController tc = new TeamController(context);
        tc.checkTeam(tc.assembleTeam(newMatch, 1), db, loggedUser, newMatch, true);
        tc.checkTeam(tc.assembleTeam(newMatch, 2), db, loggedUser, newMatch, true);

        tc.checkTeam(tc.assembleTeam(oldMatch, 1), db, loggedUser, oldMatch, false);
        tc.checkTeam(tc.assembleTeam(oldMatch, 2), db, loggedUser, oldMatch, false);

        tc.hasGame(oldMatch.getMyTeam(), db, loggedUser);
        tc.hasGame(oldMatch.getRivalTeam(), db, loggedUser);

        PlayerController pc = new PlayerController(context);
        pc.checkPlayer(pc.assemblePlayer(newMatch), db, loggedUser, newMatch, true);
        pc.checkPlayer(pc.assemblePlayer(oldMatch), db, loggedUser, newMatch, false);

        pc.hasGame(pc.assemblePlayer(oldMatch), db, loggedUser);

        db.updateMatch(newMatch, loggedUser);

        db.close();
    }



}
