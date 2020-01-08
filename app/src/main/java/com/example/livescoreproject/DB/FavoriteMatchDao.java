package com.example.livescoreproject.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.livescoreproject.classes.FavoriteMatch;

import java.util.List;

@Dao
public interface FavoriteMatchDao {
    @Query("SELECT * FROM FavoriteMatches")
    List<FavoriteMatch> getFavoriteMatches();

    @Query("SELECT * FROM FavoriteMatches WHERE userId=:userId")
    List<FavoriteMatch> getFavoriteMatchesbyUserId(int userId);

    @Query("SELECT COUNT(*) FROM FavoriteMatches WHERE date BETWEEN date(:yearStringBegin) AND date(:yearStringEnd) AND userId=:userId")
    int getNoFavoriteMatchesByYear(String yearStringBegin, String yearStringEnd, int userId);

    @Insert
    void insertFavoriteMatch(FavoriteMatch favoriteMatch);

    @Delete
    void delete(FavoriteMatch match);
}
