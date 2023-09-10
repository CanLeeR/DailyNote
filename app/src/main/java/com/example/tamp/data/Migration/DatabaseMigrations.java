package com.example.tamp.data.Migration;

import androidx.annotation.NonNull;
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
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE IF NOT EXISTS `list` (`list_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` INTEGER NOT NULL, `list_content` TEXT, `status` INTEGER NOT NULL)");
        }
    };

}
