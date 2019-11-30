package com.example.livescoreproject.classes;

import java.util.Date;

public class MatchInfo {
    private String competitionName;
    private Date matchTime;
    private String hometeam;
    private String awayteam;
    private String score;
    private String status;
    private int numberOfMatches;
    private int totalGoals;
    private int hometeamWins;
    private int hometeamDraws;
    private int hometeamLosses;
    private int awayteamWins;
    private int awayteamDraws;
    private int awayteamLosses;

    public MatchInfo() {}

    public MatchInfo(String competitionName, Date matchTime, String hometeam, String awayteam, String score, String status, int numberOfMatches, int totalGoals, int hometeamWins, int hometeamDraws, int hometeamLosses, int awayteamWins, int awayteamDraws, int awayteamLosses) {
        this.competitionName = competitionName;
        this.matchTime = matchTime;
        this.hometeam = hometeam;
        this.awayteam = awayteam;
        this.score = score;
        this.status = status;
        this.numberOfMatches = numberOfMatches;
        this.totalGoals = totalGoals;
        this.hometeamWins = hometeamWins;
        this.hometeamDraws = hometeamDraws;
        this.hometeamLosses = hometeamLosses;
        this.awayteamWins = awayteamWins;
        this.awayteamDraws = awayteamDraws;
        this.awayteamLosses = awayteamLosses;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public String getHometeam() {
        return hometeam;
    }

    public void setHometeam(String hometeam) {
        this.hometeam = hometeam;
    }

    public String getAwayteam() {
        return awayteam;
    }

    public void setAwayteam(String awayteam) {
        this.awayteam = awayteam;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumberOfMatches() {
        return numberOfMatches;
    }

    public void setNumberOfMatches(int numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public int getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(int totalGoals) {
        this.totalGoals = totalGoals;
    }

    public int getHometeamWins() {
        return hometeamWins;
    }

    public void setHometeamWins(int hometeamWins) {
        this.hometeamWins = hometeamWins;
    }

    public int getHometeamDraws() {
        return hometeamDraws;
    }

    public void setHometeamDraws(int hometeamDraws) {
        this.hometeamDraws = hometeamDraws;
    }

    public int getHometeamLosses() {
        return hometeamLosses;
    }

    public void setHometeamLosses(int hometeamLoses) {
        this.hometeamLosses = hometeamLoses;
    }

    public int getAwayteamWins() {
        return awayteamWins;
    }

    public void setAwayteamWins(int awayteamWins) {
        this.awayteamWins = awayteamWins;
    }

    public int getAwayteamDraws() {
        return awayteamDraws;
    }

    public void setAwayteamDraws(int awayteamDraws) {
        this.awayteamDraws = awayteamDraws;
    }

    public int getAwayteamLosses() {
        return awayteamLosses;
    }

    public void setAwayteamLosses(int awayteamLoses) {
        this.awayteamLosses = awayteamLoses;
    }
}
