package com.example.trabalhofinal.data.model;

public class Player {
    private String name;
    private Stats stats;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public String toString() {
        return getName() + "," + getStats().getGamesPlayed() + ","
                + getStats().getWins() + "," + getStats().getDraws() + ","
                + getStats().getDefeats() + "," + getStats().getGoalsFor()
                + "," + getStats().getGoalsAgainst() + "," + getStats().goalsDifference() + "," +
                getStats().performanceRate();
    }
}