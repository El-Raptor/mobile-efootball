package com.example.trabalhofinal.data.model;

public class Team {
    private String name;
    private Stats stats;
    private String badgePath;

    public String getBadgePath() {
        return badgePath;
    }

    public void setBadgePath() {
        this.badgePath = createPath(this.name);
    }

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

    @Override
    public String toString() {
        return getName() + "," + getStats().getGamesPlayed() + ","
                + getStats().getWins() + "," + getStats().getDraws() + ","
                + getStats().getDefeats() + "," + getStats().getGoalsFor()
                + "," + getStats().getGoalsAgainst() + "," + getStats().goalsDifference() + "," +
                getStats().performanceRate();
    }

    private String createPath(String teamName) {
        String path = "team_" + teamName;
        path = path.toLowerCase();
        path = path.replace(" ", "_");
        path = checkSpecialChar(path);

        return path;
    }

    private String checkSpecialChar(String teamName) {
        String normalTeamName = teamName;
        normalTeamName = checkA(normalTeamName);
        normalTeamName = checkE(normalTeamName);
        normalTeamName = checkI(normalTeamName);
        normalTeamName = checkO(normalTeamName);
        normalTeamName = checkU(normalTeamName);
        normalTeamName = checkC(normalTeamName);

        return normalTeamName;
    }

    private String checkA(String teamName) {
        String normalTeamName = teamName;
        normalTeamName = normalTeamName.replace("á", "a");
        normalTeamName = normalTeamName.replace("â", "a");
        normalTeamName = normalTeamName.replace("ã", "a");
        return normalTeamName;
    }

    private String checkE(String teamName) {
        String normalTeamName = teamName;
        normalTeamName = normalTeamName.replace("é", "e");
        normalTeamName = normalTeamName.replace("ê", "e");
        return normalTeamName;
    }

    private String checkI(String teamName) {
        String normalTeamName = teamName;
        normalTeamName = normalTeamName.replace("í", "i");
        return normalTeamName;
    }

    private String checkO(String teamName) {
        String normalTeamName = teamName;
        normalTeamName = normalTeamName.replace("ó", "o");
        normalTeamName = normalTeamName.replace("ô", "o");
        normalTeamName = normalTeamName.replace("õ", "o");
        return normalTeamName;
    }

    private String checkU(String teamName) {
        String normalTeamName = teamName;
        normalTeamName = normalTeamName.replace("ú", "a");
        normalTeamName = normalTeamName.replace("ü", "a");
        return normalTeamName;
    }

    private String checkC(String teamName) {
        String normalTeamName = teamName;
        normalTeamName = normalTeamName.replace("ç", "c");
        return normalTeamName;
    }
}
