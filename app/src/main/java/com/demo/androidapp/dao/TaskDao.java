package com.demo.androidapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.demo.androidapp.model.entity.Task;

import java.util.List;

@Dao
public interface TaskDao {

    //根据uid查询出所有任务
    @Query("SELECT * FROM task WHERE uid = :uid")
    public LiveData<List<Task>> getAllTaskLiveDataByUid(String uid);

    //根据uid查询出所有任务
    @Query("SELECT * FROM task")
    public List<Task> getAllTaskListByUid();

    //根据uid添加单个任务
    @Insert
    public void addTask(Task task);

    //根据uid添加多个任务
    @Insert
    public Long[] addTasks(Task... tasks);

    //根据任务内容进行模糊查询出所有相关任务
    @Query("SELECT * FROM task WHERE uid = :uid and task like :pattern")
    public LiveData<List<Task>> getTasksByTaskByPattern(String pattern,String uid);

    @Update
    void updateAllTaskFromServers(Task...tasks);

    @Delete
    void deleteTask(Task...tasks);

    @Query("DELETE FROM task WHERE uid = :uid ")
    void deleteAllTasksByUid(String uid);

}
