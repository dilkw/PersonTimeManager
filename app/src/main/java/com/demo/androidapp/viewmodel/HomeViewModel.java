package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.ReturnListObject;
import com.demo.androidapp.repository.TaskRepository;

import java.util.Arrays;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;

    public LiveData<ReturnData<ReturnListObject<Task>>> returnDataLiveData;

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
    public LiveData<ReturnData<ReturnListObject<Task>>> getAllTaskByUidInServer() {
        Log.d("imageView", "getAllTaskByUidInServer: 从服务器中获取数据");
        return taskRepository.getAllTaskByUidInServer();
    }
    //根据用户id从本地数据库加载该用户的任务列表
    public void getAllTaskByUidInDB() {
        //在数据库中没有数据时尝试从无服务器中获取
        Log.d("imageView", "getAllTaskByUidInServer: 从数据库中获取数据");
        taskRepository.getAllTaskByUidInDB();
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

    //根据用户id从本地数据库清空该用户的任务列表
    public void deleteAllTaskByUidInDB() {
        //在数据库中没有数据时尝试从无服务器中获取
        taskRepository.deleteAllTaskByUidInDB();
    }

    //根据用户id从本地数据库清空该用户的任务列表
    public void deleteTasksByUidInDB(List<Task> tasks) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (tasks == null || tasks.size() == 0) {
            return;
        }
        Task[] tasks1 = null;
        tasks.toArray(tasks1);
        taskRepository.deleteAllTaskByUidInDB(tasks1);
        taskRepository.deleteAllTaskByUidInDB();
    }

    //删除服务器中单个任务
    public LiveData<ReturnData<Object>> deleteTaskByIdsInServer(List<Task> tasks) {
        Log.d("imageView", "deleteTasksByUidInServer: 删除任务");
        if (tasks == null || tasks.size() == 0) {
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        long[] taskIds = new long[tasks.size()];
        for (int i =0 ; i < tasks.size() ; i++) {
            taskIds[i] = tasks.get(i).getId();
        }
        return taskRepository.deleteTaskByIdsInServer(Arrays.toString(taskIds));
    }

    //根据用户id从本地数据库清空并添加任务列表
    public void deleteAllTaskAndAddInDB(List<Task> tasks) {
        Log.d("imageView", "deleteAllTaskAndAddInDB: ");
        if (tasks == null || tasks.size() ==0) {
            return;
        }
        Task[] arrayTasks = new Task[tasks.size()];
        tasks.toArray(arrayTasks);
        taskRepository.deleteALLTasksAndAdd(arrayTasks);
    }
}
