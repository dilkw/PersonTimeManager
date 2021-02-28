package com.demo.androidapp.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.demo.androidapp.model.entity.CategoryOfTask;
import com.demo.androidapp.model.entity.Task;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface TaskDao {

    //根据uid查询出所有任务
    @Query("SELECT * FROM task WHERE uid = :uid")
    public LiveData<List<Task>> getAllTaskLiveDataByUid(String uid);

    //根据uid查询出所有任务
    @Query("SELECT * FROM task WHERE uid = :uid")
    public List<Task> getAllTaskListByUid(String uid);

    //根据uid添加单个任务
    @Insert
    public void addTask(Task task);

    //根据uid添加单多个任务
    @Insert
    public void addTasks(Task... tasks);

    //根据任务内容进行模糊查询出所有相关任务
    @Query("SELECT * FROM task WHERE task like :pattern")
    public List<Task> getAllTaskByTaskByPattern(String pattern);

    @Update
    void updateAllTaskFromServers(Task...tasks);

    @Delete
    void deleteTask(Task...tasks);

}
