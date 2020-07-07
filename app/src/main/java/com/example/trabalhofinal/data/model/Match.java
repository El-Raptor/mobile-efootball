package com.example.trabalhofinal.data.model;

import java.sql.Date;

public class Match {

    private int id;
    private Date matchDate;
    private String game;
    private String gameMode;
    private String rival;
    private String myTeam;
    private int goalsFor;
    private int goalsAgainst;
    private String rivalTeam;
    private Integer penaltiesGF;
    private Integer penaltiesGA;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getRival() {
        return rival;
    }

    public void setRival(String rival) {
        this.rival = rival;
    }

    public String getMyTeam() {
        return myTeam;
    }

    public void setMyTeam(String myTeam) {
        this.myTeam = myTeam;
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

    public String getRivalTeam() {
        return rivalTeam;
    }

    public void setRivalTeam(String rivalTeam) {
        this.rivalTeam = rivalTeam;
    }

    public Integer getPenaltiesGF() {
        return penaltiesGF;
    }

    public void setPenaltiesGF(Integer penaltiesGF) {
        this.penaltiesGF = penaltiesGF;
    }

    public Integer getPenaltiesGA() {
        return penaltiesGA;
    }

    public void setPenaltiesGA(Integer penaltiesGA) {
        this.penaltiesGA = penaltiesGA;
    }

    public String toString() {
        return getMatchDate() + "," + getRival() + "," + getMyTeam() + "," + getGoalsFor()
                + "," + getGoalsAgainst() + "," + getRivalTeam() + "," + getPenaltiesGF() + "," +
                getPenaltiesGA();
    }
}
