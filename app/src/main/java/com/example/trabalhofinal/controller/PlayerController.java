package com.example.trabalhofinal.controller;

import android.content.Context;

import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.Match;
import com.example.trabalhofinal.data.model.Player;
import com.example.trabalhofinal.data.model.Stats;
import com.example.trabalhofinal.data.model.User;

import java.util.List;

public class PlayerController {

    private Context context;

    public PlayerController(Context context) {
        this.context = context;
    }

    public void checkPlayer(Player player, DBHelper db, User loggedUser, Match addingMatch) {
        List<Player> playerResult = db.getPlayer(player.getName(), loggedUser);
        if (playerResult.isEmpty())
            db.addPlayer(player, loggedUser);
        else
            updatePlayer(playerResult.get(0), db, addingMatch, loggedUser);
    }

    public Player assemblePlayer(Match match) {
        Player player = new Player();
        player.setName(match.getRival());

        int goalsFor = match.getGoalsFor();
        int goalsAgainst = match.getGoalsAgainst();
        int penFor;
        int penAgainst;
        if (match.getPenaltiesGF() == null) {
            penFor = 0;
            penAgainst = 0;
        } else {
            penFor = match.getPenaltiesGF();
            penAgainst = match.getPenaltiesGA();
        }

        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setWins(0);
        stats.setDraws(0);
        stats.setDefeats(0);
        stats.setGoalsFor(match.getGoalsAgainst());
        stats.setGoalsAgainst(match.getGoalsFor());

        if (result(goalsAgainst, goalsFor, penAgainst, penFor) == 1)
            stats.setWins(1);
        else if (result(goalsAgainst, goalsFor, penAgainst, penFor) == -1)
            stats.setDefeats(1);
        else
            stats.setDraws(1);

        player.setStats(stats);
        return player;
    }

    public void updatePlayer(Player player, DBHelper db, Match addingMatch, User loggedUser) {
        Stats newStats = new Stats();

        int goalsFor = addingMatch.getGoalsFor();
        int goalsAgainst = addingMatch.getGoalsAgainst();
        int penFor;
        int penAgainst;
        if (addingMatch.getPenaltiesGF() == null) {
            penFor = 0;
            penAgainst = 0;
        } else {
            penFor = addingMatch.getPenaltiesGF();
            penAgainst = addingMatch.getPenaltiesGA();
        }

        int gamesPlayedUp = player.getStats().getGamesPlayed();
        int winsUp = player.getStats().getWins();
        int drawsUp = player.getStats().getDraws();
        int defeatsUp = player.getStats().getDefeats();
        int goalsForUp = player.getStats().getGoalsFor() + addingMatch.getGoalsAgainst();
        int goalsAgainstUp = player.getStats().getGoalsAgainst() + addingMatch.getGoalsFor();

        if (result(goalsAgainst, goalsFor, penAgainst, penFor) == 1)
            winsUp++;
        else if (result(goalsAgainst, goalsFor, penAgainst, penFor) == -1)
            defeatsUp++;
        else
            drawsUp++;

        newStats.setGamesPlayed(gamesPlayedUp + 1);
        newStats.setWins(winsUp);
        newStats.setDraws(drawsUp);
        newStats.setDefeats(defeatsUp);
        newStats.setGoalsFor(goalsForUp);
        newStats.setGoalsAgainst(goalsAgainstUp);

        player.setStats(newStats);
        db.updatePlayerStats(player, loggedUser);
    }

    private int result (int goalsFor, int goalsAgainst, int penFor, int penAgainst) {
        if (goalsFor > goalsAgainst)
            return 1;
        else if (goalsFor < goalsAgainst)
            return -1;
        else if (goalsFor == goalsAgainst) {
            if (penFor > penAgainst)
                return 1;
            else if (penFor < penAgainst)
                return -1;
        }
        return 0;
    }
}
