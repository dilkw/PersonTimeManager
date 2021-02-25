package com.demo.androidapp.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.db.AppDatabase;
import com.demo.androidapp.db.TaskDao;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.ReturnListObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRepository {

    private Api api;

    private TaskDao taskDao;

    private MutableLiveData<ReturnData> returnDataLiveData = new MutableLiveData<>();

    private TaskRepository authRepository;

    public TaskRepository(Application application) {
        this.api = MyApplication.getApi();
        this.taskDao = Room
                .databaseBuilder(application.getApplicationContext(), AppDatabase.class, "task")
                .build()
                .taskDao();
    }

    public MutableLiveData<ReturnData> getReturnDataLiveData() {
        if (returnDataLiveData == null) {
            Log.d("imageView", "getReturnDataLiveData: returnDataLiveData为空");
        }
        return returnDataLiveData;
    }

    public TaskRepository getInstance() {
        if (authRepository == null) {
            return new TaskRepository(MyApplication.getApplication());
        }
        return this;
    }

    //根据cookie在服务器中获取任务列表,cookie在发送请求的时候自动添加到header中
    public void getAllTaskByUidInService(String uid) {
        Log.d("imageView", getClass().getName() + "TaskRepository: 获取任务清单" );
        api.getTaskList().enqueue(new Callback<ReturnData<ReturnListObject<Task>>>() {
            @Override
            public void onResponse(Call<ReturnData<ReturnListObject<Task>>> call, Response<ReturnData<ReturnListObject<Task>>> response) {
                Log.d("imageView", "TaskRepository: 获取任务清单成功");
                returnDataLiveData.postValue(response.body());
            }
            @Override
            public void onFailure(Call<ReturnData<ReturnListObject<Task>>> call, Throwable t) {
                t.printStackTrace();
                Log.d("imageView", "TaskRepository: 获取任务清单失败" );
                returnDataLiveData.postValue(new ReturnData(RCodeEnum.ERROR));
            }
        });
    }

    //根据uid在本地数据库中获取任务列表
    public void getAllTaskByUidInDB(String uid) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                returnDataLiveData.postValue(new ReturnData(taskDao.getAllTaskByUid(uid)));
            }
        });
    }

    //根据TaskId（id放在cookie中）添加任务
    public void addTask(Task task) {
        api.addTask(task).enqueue(new Callback<ReturnData>() {
            @Override
            public void onResponse(Call<ReturnData> call, Response<ReturnData> response) {

            }

            @Override
            public void onFailure(Call<ReturnData> call, Throwable t) {

            }
        });
    }
}
