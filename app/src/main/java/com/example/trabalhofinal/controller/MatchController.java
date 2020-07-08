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
        tc.checkTeam(tc.assembleTeam(newMatch,1), db, loggedUser, true);
        tc.checkTeam(tc.assembleTeam(newMatch,2), db, loggedUser, true);

        PlayerController pc = new PlayerController(context);
        pc.checkPlayer(pc.assemblePlayer(newMatch), db, loggedUser, true);

        db.addMatch(newMatch, loggedUser);

        db.close();

    }

    public void updateMatch(Match oldMatch, Match newMatch, User loggedUser) {
        db = DBHelper.getInstance(context);

        newMatch.setId(oldMatch.getId());

        TeamController tc = new TeamController(context);
        Team myNewTeam = tc.assembleTeam(newMatch, 1);
        Team rivalNewTeam = tc.assembleTeam(newMatch, 2);
        Team myOldTeam = tc.assembleTeam(oldMatch, 1);
        Team rivalOldTeam = tc.assembleTeam(oldMatch, 2);
        tc.checkTeam(myNewTeam, db, loggedUser, true);
        tc.checkTeam(rivalNewTeam, db, loggedUser, true);

        tc.checkTeam(myOldTeam, db, loggedUser, false);
        tc.checkTeam(rivalOldTeam, db, loggedUser, false);

        tc.hasGame(oldMatch.getMyTeam(), db, loggedUser);
        tc.hasGame(oldMatch.getRivalTeam(), db, loggedUser);

        PlayerController pc = new PlayerController(context);
        Player newRival = pc.assemblePlayer(newMatch);
        Player oldRival = pc.assemblePlayer(oldMatch);
        pc.checkPlayer(newRival, db, loggedUser, true);
        pc.checkPlayer(oldRival, db, loggedUser, false);

        pc.hasGame(oldRival, db, loggedUser);

        db.updateMatch(newMatch, loggedUser);

        db.close();
    }

    public void deleteMatch(Match oldMatch, User loggedUser) {
        db = DBHelper.getInstance(context);

        TeamController tc = new TeamController(context);
        Team myOldTeam = tc.assembleTeam(oldMatch, 1);
        Team rivalOldTeam = tc.assembleTeam(oldMatch, 2);

        tc.checkTeam(myOldTeam, db, loggedUser, false);
        tc.checkTeam(rivalOldTeam, db, loggedUser, false);
        tc.hasGame(oldMatch.getMyTeam(), db, loggedUser);
        tc.hasGame(oldMatch.getRivalTeam(), db, loggedUser);

        PlayerController pc = new PlayerController(context);
        Player oldRival = pc.assemblePlayer(oldMatch);
        pc.checkPlayer(oldRival, db, loggedUser, false);
        pc.hasGame(oldRival, db, loggedUser);

        db.deleteMatch(oldMatch, loggedUser);

        db.close();
    }



}
