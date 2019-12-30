package com.example.livescoreproject.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.livescoreproject.classes.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM Users")
    List<User> getAll();

    @Query("SELECT * FROM Users WHERE id=:id")
    User getUser(int id);

    @Query("SELECT * FROM Users WHERE username=:username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM Users WHERE username=:username AND password=:password LIMIT 1")
    User checkUsernameAndPass(String username, String password);

    @Insert
    void addUser(User user);

    @Delete
    void deleteUser(User user);
}
