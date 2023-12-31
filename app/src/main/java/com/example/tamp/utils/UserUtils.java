package com.example.tamp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tamp.ui.activities.LoginActivity;

public class UserUtils {

    public static void saveLoggedInUserId(Context context, int userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("logged_in_user_id", userId);
        editor.apply();
    }
    public void saveLoggedInUsername(Context context, String username) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logged_in_username",username);
        editor.apply();
    }

    public static int getLoggedInUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("logged_in_user_id", -1);
    }

    public static void clearLoggedInUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("logged_in_user_id");
        editor.apply();
    }


}
