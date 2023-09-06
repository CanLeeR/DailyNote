package com.example.tamp.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tamp.data.Dao.UserDao;
import com.example.tamp.data.pojo.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
