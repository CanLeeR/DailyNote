package com.example.tamp.data.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tamp.data.entities.ToDo;

import java.util.List;

@Dao
public interface ToDoDao {


    @Insert
    long insertToDo(ToDo toDo);

    @Update
    int updateToDo(ToDo toDo);

    @Delete
    void deleteToDo(ToDo toDo);

    // 查询单一清单
    @Query("SELECT * FROM list WHERE list_id = :toDoId")
    ToDo getToDoById(long toDoId);

    @Query("SELECT * FROM list WHERE user_id = :userId ORDER BY status DESC")
    List<ToDo> getToDoByUserId(int userId);

}

