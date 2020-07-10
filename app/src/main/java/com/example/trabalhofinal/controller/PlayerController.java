package com.example.trabalhofinal.controller;

import android.content.Context;

import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.Match;
import com.example.trabalhofinal.data.model.Player;
import com.example.trabalhofinal.data.model.Stats;
import com.example.trabalhofinal.data.model.Team;
import com.example.trabalhofinal.data.model.User;

import java.util.List;

public class PlayerController {

    private Context context;

    public PlayerController(Context context) {
        this.context = context;
    }

    public void checkPlayer(Player player, DBHelper db, User loggedUser, boolean add) {
        List<Player> playerResult = db.getPlayer(player.getName(), loggedUser);
        if (playerResult.isEmpty())
            db.addPlayer(player, loggedUser);
        else {
            if (add)
                updateAddPlayer(playerResult.get(0), db, player, loggedUser);
            else
                updateRemovedPlayer(playerResult.get(0), db, player, loggedUser);

        }

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

    public void updateAddPlayer(Player playerToUpdate, DBHelper db, Player player, User loggedUser) {
        Stats newStats = new Stats();

        // Variáveis do time a atualizar.
        int goalsFor = player.getStats().getGoalsFor();
        int goalsAgainst = player.getStats().getGoalsAgainst();
        int wins = player.getStats().getWins();
        int draws = player.getStats().getDraws();
        int defeats = player.getStats().getDefeats();

        // Variáveis de atualização do time.
        int gamesPlayedUp = playerToUpdate.getStats().getGamesPlayed();
        int goalsForUp = playerToUpdate.getStats().getGoalsFor() + goalsFor;
        int goalsAgainstUp = playerToUpdate.getStats().getGoalsAgainst() + goalsAgainst;
        int winsUp = playerToUpdate.getStats().getWins() + wins;
        int drawsUp = playerToUpdate.getStats().getDraws() + draws;
        int defeatsUp = playerToUpdate.getStats().getDefeats() + defeats;

        newStats.setGamesPlayed(gamesPlayedUp + 1);
        newStats.setWins(winsUp);
        newStats.setDraws(drawsUp);
        newStats.setDefeats(defeatsUp);
        newStats.setGoalsFor(goalsForUp);
        newStats.setGoalsAgainst(goalsAgainstUp);

        playerToUpdate.setStats(newStats);
        db.updatePlayerStats(playerToUpdate, loggedUser);
    }

    public void updateRemovedPlayer(Player playerToUpdate, DBHelper db, Player player, User loggedUser) {
        Stats newStats = new Stats();

        // Variáveis do time a atualizar.
        int goalsFor = player.getStats().getGoalsFor();
        int goalsAgainst = player.getStats().getGoalsAgainst();
        int wins = player.getStats().getWins();
        int draws = player.getStats().getDraws();
        int defeats = player.getStats().getDefeats();

        // Variáveis de atualização do time.
        int gamesPlayedUp = playerToUpdate.getStats().getGamesPlayed();
        int goalsForUp = playerToUpdate.getStats().getGoalsFor() - goalsFor;
        int goalsAgainstUp = playerToUpdate.getStats().getGoalsAgainst() - goalsAgainst;
        int winsUp = playerToUpdate.getStats().getWins() - wins;
        int drawsUp = playerToUpdate.getStats().getDraws() - draws;
        int defeatsUp = playerToUpdate.getStats().getDefeats() - defeats;

        newStats.setGamesPlayed(gamesPlayedUp - 1);
        newStats.setWins(winsUp);
        newStats.setDraws(drawsUp);
        newStats.setDefeats(defeatsUp);
        newStats.setGoalsFor(goalsForUp);
        newStats.setGoalsAgainst(goalsAgainstUp);

        playerToUpdate.setStats(newStats);
        db.updatePlayerStats(playerToUpdate, loggedUser);
    }

    public void hasGame(Player oldPlayer, DBHelper db, User loggedUser) {
        if (oldPlayer.getStats().getGamesPlayed() == 0) {
            db.deletePlayer(oldPlayer, loggedUser);
        }
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
