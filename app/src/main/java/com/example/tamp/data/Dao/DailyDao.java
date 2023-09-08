package com.example.tamp.data.Dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tamp.data.entities.Daily;

import java.util.List;

@Dao
public interface DailyDao {

    /**
     * 插入日记
     *
     * @param user
     */
    @Insert
    void insertDaily(Daily user);

    /**
     * 根据用户id查找他的日记
     *
     * @param userId
     * @return
     */
    @Query("SELECT * FROM daily WHERE  user_id= :userId")
    List<Daily> getByUserId(Integer userId);

    /**
     * 更新日记
     *
     * @param daily
     */
    @Update
    void updateDaily(Daily daily);

    /**
     * 删除日记
     *
     * @param daily
     */
    @Delete
    void deleteDaily(Daily daily);
}
