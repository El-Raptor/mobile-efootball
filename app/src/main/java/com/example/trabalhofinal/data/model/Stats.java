package com.example.trabalhofinal.data.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Stats {
    private int gamesPlayed;
    private int wins;
    private int draws;
    private int defeats;
    private int goalsFor;
    private int goalsAgainst;

    public int goalsDifference() {
        return this.goalsFor - this.goalsAgainst;
    }

    public double performanceRate() {

        int pointsWon = 3 * this.wins + this.draws;
        int totalPoints = 3 * this.gamesPlayed;
        double perfRate = (Double.valueOf(pointsWon) / Double.valueOf(totalPoints)) * 100;

        BigDecimal bd = new BigDecimal(perfRate).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getDefeats() {
        return defeats;
    }

    public void setDefeats(int defeats) {
        this.defeats = defeats;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }
}

