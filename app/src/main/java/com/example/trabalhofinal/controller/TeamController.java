package com.example.trabalhofinal.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.Match;
import com.example.trabalhofinal.data.model.Stats;
import com.example.trabalhofinal.data.model.Team;
import com.example.trabalhofinal.data.model.User;

import java.util.List;

public class TeamController {

    private Context context;

    public TeamController(Context context) {
        this.context = context;
    }

    public Drawable getTeamBadge(String badgePath) {
        Resources res = context.getResources();
        int resId = res.getIdentifier(badgePath, "drawable", context.getPackageName());
        Drawable drawable = res.getDrawable(resId);
        return drawable;
    }

    public void checkTeam(Team team, DBHelper db, User loggedUser, Match addingMatch) {
        List<Team> teamResult = db.getTeam(team.getName(), loggedUser);
        if (teamResult.isEmpty())
            db.addTeam(team, loggedUser);

        else
            updateTeam(teamResult.get(0), db, addingMatch, loggedUser);
    }

    private void updateTeam(Team updatingTeam, DBHelper db, Match addingMatch, User loggedUser) {
        Stats newStats = new Stats();
        int gamesPlayedUp = updatingTeam.getStats().getGamesPlayed();
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

        if (updatingTeam.getName() == addingMatch.getMyTeam()) {
            int goalsForUp = updatingTeam.getStats().getGoalsFor() + addingMatch.getGoalsFor();
            int goalsAgainstUp = updatingTeam.getStats().getGoalsAgainst() + addingMatch.getGoalsAgainst();
            int winsUp = updatingTeam.getStats().getWins();
            int drawsUp = updatingTeam.getStats().getDraws();
            int defeatsUp = updatingTeam.getStats().getDefeats();

            if (result(goalsFor, goalsAgainst, penFor, penAgainst) == 1)
                winsUp++;
            else if (result(goalsFor, goalsAgainst, penFor, penAgainst) == -1)
                defeatsUp++;
            else
                drawsUp++;

            newStats.setGamesPlayed(gamesPlayedUp + 1);
            newStats.setWins(winsUp);
            newStats.setDraws(drawsUp);
            newStats.setDefeats(defeatsUp);
            newStats.setGoalsFor(goalsForUp);
            newStats.setGoalsAgainst(goalsAgainstUp);

        } else if (updatingTeam.getName() == addingMatch.getRivalTeam()) {
            int goalsForUp = updatingTeam.getStats().getGoalsFor() + addingMatch.getGoalsAgainst();
            int goalsAgainstUp = updatingTeam.getStats().getGoalsAgainst() + addingMatch.getGoalsFor();
            int winsUp = updatingTeam.getStats().getWins();
            int drawsUp = updatingTeam.getStats().getDraws();
            int defeatsUp = updatingTeam.getStats().getDefeats();

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
        }

        updatingTeam.setStats(newStats);
        db.updateTeamStats(updatingTeam, loggedUser);
    }

    public Team assembleTeam(Match match, int homerOrAway) {
        Team team = new Team();
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

        if (homerOrAway == 1) {
            team.setName(match.getMyTeam());
            team.setBadgePath();
            Stats stats = new Stats();
            stats.setGamesPlayed(1);
            stats.setWins(0);
            stats.setDefeats(0);
            stats.setDraws(0);
            stats.setGoalsFor(match.getGoalsFor());
            stats.setGoalsAgainst(match.getGoalsAgainst());

            if (result(goalsFor, goalsAgainst, penFor, penAgainst) == 1)
                stats.setWins(1);
            else if (result(goalsFor, goalsAgainst, penFor, penAgainst) == -1)
                stats.setDefeats(1);
            else
                stats.setDraws(1);

            team.setStats(stats);
            return team;
        }

        team.setName(match.getRivalTeam());
        team.setBadgePath();
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

        team.setStats(stats);
        return team;
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