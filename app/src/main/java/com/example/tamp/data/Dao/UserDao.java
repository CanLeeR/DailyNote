package com.example.tamp.data.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tamp.data.entities.User;

@Dao
public interface UserDao {

    /**
     * 插入用户
     * @param user
     */
    @Insert
    void insertUser(User user);

    /**
     * 根据名字查找
     * @param username
     * @return
     */
    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);
}
