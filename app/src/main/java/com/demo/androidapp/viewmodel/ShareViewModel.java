package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.repository.ClockRepository;
import com.demo.androidapp.repository.TaskRepository;

import java.util.Arrays;
import java.util.List;

public class ShareViewModel extends AndroidViewModel {

    private ClockRepository clockRepository;

    private TaskRepository taskRepository;

    public ShareViewModel(@NonNull Application application) {
        super(application);
        Log.d("imageView", "HomeViewModel:-=-=-=-=-= " + application.getClass().getName());
        clockRepository = new ClockRepository(application);
        taskRepository = new TaskRepository(application);
    }

    //在数据库中获取所有时钟数据
    public LiveData<List<Clock>> getAllClocksLiveDataInDB() {
        return clockRepository.getAllClocksLiveDataInDB();
    }

    //通过模糊查询出与关键子相似的时钟
    public LiveData<List<Clock>> getClocksLiveDataByPatternInDB(String pattern) {
        return clockRepository.getClocksLiveDataByPatternInDB(pattern);
    }

    //根据用户id从本地数据库加载该用户的任务列表
    public LiveData<List<Task>> getAllTaskLiveDataByUidInDB() {
        //在数据库中没有数据时尝试从无服务器中获取
        Log.d("imageView", "getAllTaskByUidInServer: 从数据库中获取数据");
        return taskRepository.getAllTaskLiveDataByUidInDB();
    }

    //根据用户id和任务详情从本地数据库加载该用户的任务列表
    public LiveData<List<Task>> getTasksLiveDataByPatternInDB(String pattern) {
        //在数据库中没有数据时尝试从无服务器中获取
        Log.d("imageView", "getAllTaskByUidInServer: 从数据库中获取数据");
        return taskRepository.getTasksLiveDataByPatternInDB(pattern);
    }

}