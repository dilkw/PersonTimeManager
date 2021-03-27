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
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.returnObject.ReturnListObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClockRepository {

    private Api api;

    private ClockDao clockDao;

    private MutableLiveData<ReturnData<List<Clock>>> returnDataLiveData;

    private MutableLiveData<ReturnData> deleteReturnDataLiveData;

    private ClockRepository authRepository;

    public ClockRepository(Application application) {
        this.api = MyApplication.getApi();
        this.clockDao = Room
                .databaseBuilder(application.getApplicationContext(), AppDatabase.class, "clock")
                .build()
                .clockDao();
        returnDataLiveData = new MutableLiveData<>();
        //returnDataLiveData = clockDao.getAllClockLiveDataByUid(MyApplication.getApplication().getUID());
    }


    //从服务器中获取时钟列表
    public void getAllClocksInServer() {
        api.getAllClocks().enqueue(new Callback<ReturnData<ReturnListObject<Clock>>>() {
            @Override
            public void onResponse(Call<ReturnData<ReturnListObject<Clock>>> call, Response<ReturnData<ReturnListObject<Clock>>> response) {
                returnDataLiveData.postValue(new ReturnData<List<Clock>>(response.body().getCode(),response.body().getMsg(),response.body().getData().getItems()));
                if (response.body().getData().getTotal() > 0) {
                    Clock[] clocks = new Clock[response.body().getData().getTotal()];
                    response.body().getData().getItems().toArray(clocks);
                    deleteAllClocksAndAdd(clocks);
                }
            }

            @Override
            public void onFailure(Call<ReturnData<ReturnListObject<Clock>>> call, Throwable t) {
                returnDataLiveData.postValue(new ReturnData<List<Clock>>(RCodeEnum.ERROR));
            }
        });
    }

    public LiveData<ReturnData<List<Clock>>> getLiveData() {
        if (returnDataLiveData == null) {
            Log.d("imageView", "getReturnDataLiveData: returnDataLiveData为空");
        }
        return returnDataLiveData;
    }

    //通过模糊查询出相关时钟
    public LiveData<List<Clock>> getClocksLiveDataByPatternInDB(String pattern) {
        String uid = MyApplication.getApplication().getUser().getUid();
        return clockDao.getAllClockByByPattern("%" + pattern + "%",uid);
    }

    //根据某个时钟id获取时钟信息（网络数据库）
    public LiveData<ReturnData<Clock>> getClockInfoInServer(long clockId) {
        return api.getClockInfoByClockId(clockId);
    }
    //根据某个时钟id获取时钟信息（本地数据库）
    public LiveData<Clock> getClockInfoInDB(long clockId) {
        return clockDao.getClockByClockId(clockId);
    }

    //更新时钟信息（网络数据库）
    public LiveData<ReturnData<Object>> upDateClock(Clock clock) {
        return api.upDateClock(clock);
    }

    //根据uid在本地数据库中获取任务列表
    public LiveData<List<Clock>> getAllClocksLiveDataInDB() {
        String uid = MyApplication.getApplication().getUser().getUid();
        return clockDao.getAllClockLiveDataByUid(uid);
    }

    //在本地数据库中删除多个数据
    public void deleteClocksByUidInDB(Clock... clocks) {
        String uid = MyApplication.getApplication().getUser().getUid();
        Log.d("imageView", "deleteClocksByUidInDB: 数据库删除数据");
        new DeleteClocks(clockDao).execute(clocks);
    }

    //在服务器中删除单个数据参数为数组形式的字符串"[1,2,3]"
    public LiveData<ReturnData<Object>> deleteClockByClockIdInServer(String clockIds) {
        String uid = MyApplication.getApplication().getUser().getUid();
        Log.d("imageView", "deleteClockByClockIdInServer: 删除数据" + clockIds);
        return api.deleteClock(clockIds);
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

    //插入本地数据库任务列表
    public LiveData<ReturnData<Clock>> addClockToServer(Clock clock) {
        return api.addClock(clock);
    }

    //更新本地数据库任务列表
    public void updateClocksInDB(Clock... clocks) {
        new ClockRepository.UpdateClockList(clockDao).execute(clocks);
    }

    //更新本地数据库任务列表
    public LiveData<ReturnData<Object>> updateClocksInServer(Clock clock) {
        return api.updateClock(clock.getId(),clock);
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

    //删除并更新数据
    public void deleteAllClocksAndAdd(Clock... clocks) {
        new DeleteAllClocksAndAdd(clockDao,this).equals(clocks);
    }
    //删除并更新数据
    public static class DeleteAllClocksAndAdd extends AsyncTask<Clock,Void,Void> {

        ClockDao clockDao;

        ClockRepository clockRepository;

        DeleteAllClocksAndAdd(ClockDao clockDao, ClockRepository clockRepository) {
            this.clockDao = clockDao;
            this.clockRepository = clockRepository;
        }

        Clock[] clocks;

        @Override
        protected Void doInBackground(Clock... clocks) {
            this.clocks = clocks;
            clockDao.deleteAllClocksByUid(MyApplication.getApplication().getUser().getUid());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            clockRepository.addClocksToDB(clocks);
        }
    }

}
