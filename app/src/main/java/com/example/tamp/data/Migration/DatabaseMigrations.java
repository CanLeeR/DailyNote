package com.example.tamp.data.Migration;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseMigrations {


    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE daily (" +
                            "daily_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "user_id INTEGER," +
                            "title TEXT," +
                            "content TEXT," +
                            "date TEXT)"
            );

        }
    };
}
