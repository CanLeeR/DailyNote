package com.example.tamp.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "list")
public class ToDo {

    @ColumnInfo(name = "list_id")
    @PrimaryKey(autoGenerate = true)
    long listId;

    @ColumnInfo(name = "user_id")
    int userId;

    @ColumnInfo(name = "list_content")
    String listContent;

    @ColumnInfo(name = "status")
    boolean status;

    public long getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getListContent() {
        return listContent;
    }

    public void setListContent(String listContent) {
        this.listContent = listContent;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ToDo() {
    }

    public ToDo(Integer userId, String listContent, Boolean status) {
        this.userId = userId;
        this.listContent = listContent;
        this.status = status;
    }
}
