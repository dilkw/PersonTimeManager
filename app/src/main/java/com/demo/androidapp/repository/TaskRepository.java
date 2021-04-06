package com.demo.androidapp.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.dao.AppDatabase;
import com.demo.androidapp.dao.TaskDao;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.ReturnListObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TaskRepository {

    private Api api;

    private TaskDao taskDao;

    private MutableLiveData<ReturnData<List<Task>>> returnDataLiveData;

    private MutableLiveData<ReturnData> deleteReturnDataLiveData;

    private TaskRepository authRepository;

    public TaskRepository(Application application) {
        this.api = MyApplication.getApi();
        this.taskDao = Room
                .databaseBuilder(application.getApplicationContext(), AppDatabase.class, "task")
                .build()
                .taskDao();
        returnDataLiveData = new MutableLiveData<ReturnData<List<Task>>>();
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
    public LiveData<ReturnData<ReturnListObject<Task>>> getAllTaskByUidInServer() {
        Log.d("imageView", getClass().getName() + "TaskRepository: 获取任务清单" );
        return api.getTaskList();
    }

    //根据uid在本地数据库中获取任务列表
    public void getAllTaskByUidInDB() {
        String uid = MyApplication.getApplication().getUser().getUid();
        Log.d("imageView", "getAllTaskByUidInDB: 数据库获取数据");
        new GetAllTaskByUid(taskDao,returnDataLiveData).execute(uid);
    }

    //根据uid在本地数据库中获取任务列表
    public LiveData<List<Task>> getAllTaskLiveDataByUidInDB() {
        String uid = MyApplication.getApplication().getUser().getUid();
        return taskDao.getAllTaskLiveDataByUid(uid);
    }

    //根据uid在本地数据库中获取任务列表
    public LiveData<List<Task>> getTasksLiveDataByPatternInDB(String pattern) {
        String uid = MyApplication.getApplication().getUser().getUid();
        return taskDao.getTasksByTaskByPattern("%" + pattern + "%",uid);
    }

    //在本地数据库中删除数据
    public void deleteAllTaskByUidInDB(Task... tasks) {
        String uid = MyApplication.getApplication().getUser().getUid();
        Log.d("imageView", "getAllTaskByUidInDB: 数据库删除数据");
        new DeleteAllTasks(taskDao).execute(tasks);
    }

    //在服务器中删除数据(可以多个)
    public LiveData<ReturnData<Object>> deleteTaskByIdsInServer(String taskIds) {
        Log.d("imageView", "deleteTaskByUidInServer: 服务器删除数据" + taskIds);
        return api.deleteTasksByIds(taskIds);
    }


    //插入本地数据库任务列表
    public Long[] addTasksToDB(Task... tasks) {
        Long[] longs = null;
        try {
            longs = new InsertTaskList(taskDao).execute(tasks).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return longs;
    }

    //添加到服务器任务列表
    public LiveData<ReturnData<Task>> addTasksToServer(Task task) {
        return api.addTask(task);
    }

    //更新任务到服务器
    public LiveData<ReturnData<Object>> updateTaskInServer(long id,Task task) {
        Log.d("imageView", "updateTaskInServer: " + task.toString());
        return api.updateTask(id,task);
    }

    //更新本地数据库任务列表
    public void updateTasks(Task... tasks) {
        new TaskRepository.UpdateTaskList(taskDao).execute(tasks);
    }

    public static class InsertTaskList extends AsyncTask<Task,Void, Long[]> {

        TaskDao taskDao;

        InsertTaskList(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Long[] doInBackground(Task... tasks) {
            return this.taskDao.addTasks(tasks);
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
//            List<Task> tasks = taskDao.getAllTaskListByUid(strings[0]);
//            Log.d("imageView", "doInBackground: 本地数据库数据长度" + tasks.size());
//            returnDataLiveData.postValue(new ReturnData<List<Task>>(RCodeEnum.DB_OK,tasks));
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

    public void reLogin(){
        String userName = MyApplication.getApplication().getUser().getUid();
        String password = MyApplication.getApplication().getUser().getPassword();
//        api.signIn()
    }


    public void deleteALLTasksAndAdd(Task ... tasks) {
        Log.d("imageView", "deleteAllTaskAndAddInDB: ");
        new DeleteALLTasksAndAdd(taskDao,this).execute(tasks);
    }
    //删除并更新数据
    public static class DeleteALLTasksAndAdd extends AsyncTask<Task,Void,Void> {

        TaskDao taskDao;

        TaskRepository taskRepository;

        DeleteALLTasksAndAdd(TaskDao taskDao,TaskRepository taskRepository) {
            Log.d("imageView", "deleteAllTaskAndAddInDB: ");
            this.taskDao = taskDao;
            this.taskRepository = taskRepository;
        }

        Task[] tasks;

        @Override
        protected Void doInBackground(Task... tasks) {
            Log.d("imageView", "deleteAllTaskAndAddInDB:2 ");
            this.tasks = tasks;
            taskDao.deleteAllTasksByUid(MyApplication.getApplication().getUser().getUid());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("imageView", "deleteAllTaskAndAddInDB:3 ");
            taskRepository.addTasksToDB(tasks);
        }
    }

}
