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
        tc.checkTeam(tc.assembleTeam(newMatch,1), db, loggedUser, newMatch);
        tc.checkTeam(tc.assembleTeam(newMatch,2), db, loggedUser, newMatch);

        PlayerController pc = new PlayerController(context);
        pc.checkPlayer(pc.assemblePlayer(newMatch), db, loggedUser, newMatch);

        db.addMatch(newMatch, loggedUser);

        db.close();

    }



}
