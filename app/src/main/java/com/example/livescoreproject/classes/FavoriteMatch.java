package com.example.livescoreproject.classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.example.livescoreproject.DB.Converters;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "FavoriteMatches",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns="id",
                childColumns = "userId",
                onDelete = CASCADE))
public class FavoriteMatch {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String homeTeam;
    private String awayTeam;
    private String score;
    private Date date;
    private int userId;

    public FavoriteMatch(String homeTeam, String awayTeam, String score, Date date, int userId) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.score = score;
        this.date = date;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "FavoriteMatch{" +
                "id=" + id +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", score=" + score +
                ", date=" + date +
                ", userId=" + userId +
                '}';
    }
}
