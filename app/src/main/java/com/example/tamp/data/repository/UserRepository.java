package com.example.tamp.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class UserRepository {
    private final SharedPreferences sharedPreferences;

    public UserRepository(Context context) {
        sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    public int getLoggedInUserId() {
        return sharedPreferences.getInt("logged_in_user_id", -1);
    }

    public String getUserName() {
        return sharedPreferences.getString("logged_in_username","CanLee");
    }
}
