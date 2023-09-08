package com.example.tamp.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "daily")
public class Daily {

    @ColumnInfo(name = "daily_id")
    @PrimaryKey(autoGenerate = true)
    private Integer  dailyId;
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "date")
    private LocalDate date;
    @ColumnInfo(name = "user_id")
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public Integer getDailyId() {
        return dailyId;
    }

    public void setDailyId(Integer dailyId) {
        this.dailyId = dailyId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Ignore
    public Daily(Integer userId, String title, String content, LocalDate date) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = date;
    }
    @Ignore
    public Daily( String title, String content) {
        this.title = title;
        this.content = content;
    }


    public Daily(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
