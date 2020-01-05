package com.example.livescoreproject.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.livescoreproject.classes.FavoriteMatch;
import com.example.livescoreproject.classes.User;

@Database(entities = {User.class, FavoriteMatch.class},
        exportSchema = false,
        version = 6)
@TypeConverters({Converters.class})
public abstract class LivescoreDB extends RoomDatabase {
    static private LivescoreDB instance;
    private static final String DB_NAME = "livescore.db";

    public static synchronized LivescoreDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, LivescoreDB.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

    public abstract UserDao getUserDao();

    public abstract FavoriteMatchDao getFavoriteMatchDao();

}

