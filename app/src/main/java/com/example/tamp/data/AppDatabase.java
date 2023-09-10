package com.example.tamp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.tamp.data.Dao.DailyDao;
import com.example.tamp.data.Dao.ToDoDao;
import com.example.tamp.data.Dao.UserDao;
import com.example.tamp.data.entities.Daily;
import com.example.tamp.data.entities.ToDo;
import com.example.tamp.data.entities.User;

@Database(entities = {User.class, Daily.class, ToDo.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract DailyDao dailyDao();
    public  abstract ToDoDao toDoDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "daily")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
