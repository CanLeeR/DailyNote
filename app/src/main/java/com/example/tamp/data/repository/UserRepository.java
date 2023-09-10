package com.example.tamp.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tamp.data.AppDatabase;
import com.example.tamp.data.Dao.DailyDao;
import com.example.tamp.data.Dao.ToDoDao;
import com.example.tamp.data.Dao.UserDao;
import com.example.tamp.data.entities.Daily;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserRepository {
    private final SharedPreferences sharedPreferences;
    private UserDao userDao;



    public UserRepository(Context context) {
        sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
    }

    public int getLoggedInUserId() {
        return sharedPreferences.getInt("logged_in_user_id", -1);
    }

    public String getUserName() {
        return sharedPreferences.getString("logged_in_username", "CanLee");
    }


    public void getDiaryCount(Callback callback) {
        int userId = sharedPreferences.getInt("logged_in_user_id", -1);
        Executors.newSingleThreadExecutor().execute(() -> {
            int count = userDao.getDiaryCountForUser(userId);
            callback.onResult(count);
        });
    }

    public void getToDoCount(Callback callback) {
        int userId = sharedPreferences.getInt("logged_in_user_id", -1);
        Executors.newSingleThreadExecutor().execute(() -> {
            int count = userDao.getToDoCountForUser(userId);
            callback.onResult(count);
        });
    }

    public interface Callback {
        void onResult(int count);
    }

}
