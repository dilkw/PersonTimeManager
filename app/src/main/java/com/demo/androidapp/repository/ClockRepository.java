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
import com.demo.androidapp.db.ClockDao;
import com.demo.androidapp.db.TaskDao;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Task;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClockRepository {

    private Api api;

    private ClockDao clockDao;

    private LiveData<List<Clock>> returnDataLiveData;

    private MutableLiveData<ReturnData> deleteReturnDataLiveData;

    private ClockRepository authRepository;

    public ClockRepository(Application application) {
        this.api = MyApplication.getApi();
        this.clockDao = Room
                .databaseBuilder(application.getApplicationContext(), AppDatabase.class, "clock")
                .build()
                .clockDao();
        returnDataLiveData = clockDao.getAllClockLiveDataByUid(MyApplication.getApplication().getUID());
    }

    public LiveData<List<Clock>> getLiveData() {
        if (returnDataLiveData == null) {
            Log.d("imageView", "getReturnDataLiveData: returnDataLiveData为空");
        }
        return returnDataLiveData;
    }

    public LiveData<List<Clock>> getClocksLiveDataByPattern(String s) {
        return clockDao.getAllClockByByPattern("%" + s + "%");
    }

    //根据uid在本地数据库中获取任务列表
    public void getAllTaskByUidInDB() {
        String uid = MyApplication.getApplication().getUID();
        new GetAllClockByUid(clockDao,returnDataLiveData).execute(uid);
    }

    //在本地数据库中删除多个数据
    public void deleteClocksByUidInDB(Clock... clocks) {
        String uid = MyApplication.getApplication().getUID();
        Log.d("imageView", "getAllTaskByUidInDB: 数据库删除数据");
        new DeleteClocks(clockDao).execute(clocks);
    }

    //插入本地数据库任务列表
    public Long[] addClocksToDB(Clock... clocks) {
        Long[] longs = null;
        try {
            longs = new InsertClockList(clockDao).execute(clocks).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return longs;
    }

    //更新本地数据库任务列表
    public void updateTasks(Clock... clocks) {
        new ClockRepository.UpdateClockList(clockDao).execute(clocks);
    }

    public static class InsertClockList extends AsyncTask<Clock,Void, Long[]> {

        ClockDao clockDao;

        InsertClockList(ClockDao clockDao) {
            this.clockDao = clockDao;
        }

        @Override
        protected Long[] doInBackground(Clock... clocks) {
            return this.clockDao.addClocks(clocks);
        }
    }

    public static class GetAllClockByUid extends AsyncTask<String,Void,Void> {

        ClockDao clockDao;

        private LiveData<List<Clock>> returnDataLiveData;

        GetAllClockByUid(ClockDao clockDao,LiveData<List<Clock>> returnDataLiveData) {
            this.clockDao = clockDao;
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

    public static class UpdateClockList extends AsyncTask<Clock,Void,Void> {

        ClockDao clockDao;

        UpdateClockList(ClockDao clockDao) {
            this.clockDao = clockDao;
        }

        @Override
        protected Void doInBackground(Clock... clocks) {
            this.clockDao.updateAllClockFromServers(clocks);
            return null;
        }
    }

    public static class DeleteClocks extends AsyncTask<Clock,Void,Void> {

        ClockDao clockDao;

        DeleteClocks(ClockDao clockDao) {
            this.clockDao = clockDao;
        }

        @Override
        protected Void doInBackground(Clock... clocks) {
            clockDao.deleteClock(clocks);
            return null;
        }
    }

}
