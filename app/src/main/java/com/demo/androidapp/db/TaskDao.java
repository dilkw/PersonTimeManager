package com.demo.androidapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.demo.androidapp.model.entity.Task;

import java.util.List;

@Dao
public interface TaskDao {

    //根据uid查询出所有任务
    @Query("SELECT * FROM task WHERE uid = :uid")
    public List<Task> getAllTaskByUid(String uid);

    //根据uid添加任务
    @Insert
    public void addTaskByUid(Task task);

    //根据任务内容进行模糊查询出所有相关任务
    @Query("SELECT * FROM task WHERE task like :pattern")
    public List<Task> getAllTaskByTaskByPattern(String pattern);

}
