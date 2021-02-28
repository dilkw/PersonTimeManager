package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        Log.d("imageView", "HomeViewModel:-=-=-=-=-= " + application.getClass().getName());
        taskRepository = new TaskRepository(application);
    }
    // TODO: Implement the ViewModel

    public MutableLiveData<ReturnData<List<Task>>> getReturnLiveData() {
        return taskRepository.getReturnDataLiveData();
    }

    //根据用户id从服务器中中加载该用户的任务列表
    public void getAllTaskByUidInServer() {
        //在数据库中没有数据时尝试从无服务器中获取
        taskRepository.getAllTaskByUidInServer();
    }

    //根据用户id从本地数据库加载该用户的任务列表
    public void getAllTaskByUidInDB() {
        //在数据库中没有数据时尝试从无服务器中获取
        taskRepository.getAllTaskByUidInDB();
    }

    //根据用户id从本地数据库清空该用户的任务列表
    public void deleteAllTaskByUidInDB() {
        //在数据库中没有数据时尝试从无服务器中获取
        taskRepository.deleteAllTaskByUidInDB();
    }

}
