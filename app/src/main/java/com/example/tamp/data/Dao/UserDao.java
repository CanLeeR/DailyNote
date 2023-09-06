package com.example.tamp.data.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tamp.data.models.User;

@Dao
public interface UserDao {

    //插入用户
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    //更具名字查找
    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);
}
