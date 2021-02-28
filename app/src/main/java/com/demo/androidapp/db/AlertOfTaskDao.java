package com.demo.androidapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.demo.androidapp.model.entity.AlertOfTask;

@Dao
public interface AlertOfTaskDao {

    //为任务添加提醒
    @Insert
    void addAlertOfTask(AlertOfTask... alertOfTasks);

    //删除
    @Delete
    void deleteAlertOfTask(AlertOfTask... alertOfTasks);

}
