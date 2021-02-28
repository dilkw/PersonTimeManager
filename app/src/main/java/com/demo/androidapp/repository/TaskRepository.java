package com.demo.androidapp.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRepository {

    private Api api;

    private TaskDao taskDao;

    private MutableLiveData<ReturnData<List<Task>>> returnDataLiveData;

    private TaskRepository authRepository;

    public TaskRepository(Application application) {
        this.api = MyApplication.getApplication().getApi();
        this.taskDao = Room
                .databaseBuilder(application.getApplicationContext(), AppDatabase.class, "task")
                .build()
                .taskDao();
        returnDataLiveData = new MutableLiveData<ReturnData<List<Task>>>(new ReturnData<>(new ArrayList<Task>()));
    }

    public MutableLiveData<ReturnData<List<Task>>> getReturnDataLiveData() {
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
    public void getAllTaskByUidInServer() {
        Log.d("imageView", getClass().getName() + "TaskRepository: 获取任务清单" );
        api.getTaskList().enqueue(new Callback<ReturnData<ReturnListObject<Task>>>() {
            @Override
            public void onResponse(Call<ReturnData<ReturnListObject<Task>>> call, Response<ReturnData<ReturnListObject<Task>>> response) {
                Log.d("imageView", "TaskRepository: 获取任务清单成功");
                returnDataLiveData.postValue(new ReturnData<List<Task>>(RCodeEnum.SERVER_OK,response.body().getData().getItems()));
                Task[] tasks = new Task[response.body().getData().getTotal()];
                tasks = ((response.body().getData().getItems()).toArray(tasks));
                deleteAllTaskByUidInDB(tasks);
                addTasksToDB(tasks);
            }
            @Override
            public void onFailure(Call<ReturnData<ReturnListObject<Task>>> call, Throwable t) {
                t.printStackTrace();
                Log.d("imageView", "TaskRepository: 获取任务清单失败" );
                returnDataLiveData.postValue(new ReturnData<List<Task>>(RCodeEnum.DB_ERROR));
            }
        });
    }

    //根据uid在本地数据库中获取任务列表
    public void getAllTaskByUidInDB() {
        String uid = MyApplication.getApplication().getUID();
        Log.d("imageView", "getAllTaskByUidInDB: 数据库获取数据");
        new GetAllTaskByUid(taskDao,returnDataLiveData).execute(uid);
    }

    public void deleteAllTaskByUidInDB(Task... tasks) {
        String uid = MyApplication.getApplication().getUID();
        Log.d("imageView", "getAllTaskByUidInDB: 数据库获取数据888");
        new DeleteAllTasks(taskDao).execute(tasks);
    }


    //插入本地数据库任务列表
    public void addTasksToDB(Task... tasks) {
        new TaskRepository.InsertTaskList(taskDao).execute(tasks);
    }

    //更新本地数据库任务列表
    public void updateTasks(Task... tasks) {
        new TaskRepository.UpdateTaskList(taskDao).execute(tasks);
    }

    public static class InsertTaskList extends AsyncTask<Task,Void,Void> {

        TaskDao taskDao;

        InsertTaskList(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            this.taskDao.addTasks(tasks);
            return null;
        }
    }

    public static class GetAllTaskByUid extends AsyncTask<String,Void,Void> {

        TaskDao taskDao;

        private MutableLiveData<ReturnData<List<Task>>> returnDataLiveData;

        GetAllTaskByUid(TaskDao taskDao,MutableLiveData<ReturnData<List<Task>>> returnDataLiveData) {
            this.taskDao = taskDao;
            this.returnDataLiveData = returnDataLiveData;
        }

        @Override
        protected Void doInBackground(String... strings) {
            List<Task> tasks = taskDao.getAllTaskListByUid(strings[0]);
            Log.d("imageView", "doInBackground: 本地数据库数据长度" + tasks.size());
            returnDataLiveData.postValue(new ReturnData<List<Task>>(RCodeEnum.DB_OK,tasks));
            return null;
        }
    }

    public static class UpdateTaskList extends AsyncTask<Task,Void,Void> {

        TaskDao taskDao;

        UpdateTaskList(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            this.taskDao.updateAllTaskFromServers(tasks);
            return null;
        }
    }

    public static class DeleteAllTasks extends AsyncTask<Task,Void,Void> {

        TaskDao taskDao;

        DeleteAllTasks(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.deleteTask(tasks);
            return null;
        }
    }


    //根据TaskId（id放在cookie中）添加任务
    public void addTaskToServer(Task task) {
        api.addTask(task).enqueue(new Callback<ReturnData>() {
            @Override
            public void onResponse(Call<ReturnData> call, Response<ReturnData> response) {

            }

            @Override
            public void onFailure(Call<ReturnData> call, Throwable t) {

            }
        });
    }

    public void reLogin(){
        String userName = MyApplication.getApplication().getUSER_NAME();
        String password = MyApplication.getApplication().getPASSWORD();
//        api.signIn()
    }

}