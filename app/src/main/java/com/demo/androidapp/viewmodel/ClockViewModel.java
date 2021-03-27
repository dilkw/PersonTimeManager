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
import com.demo.androidapp.repository.ClockRepository;

import java.util.Arrays;
import java.util.List;

public class ClockViewModel extends AndroidViewModel {

    private ClockRepository clockRepository;

    public ClockViewModel(@NonNull Application application) {
        super(application);
        Log.d("imageView", "HomeViewModel:-=-=-=-=-= " + application.getClass().getName());
        clockRepository = new ClockRepository(application);
    }

    //获取clockRepository中的returnLiveData
    public LiveData<ReturnData<List<Clock>>> getReturnLiveData() {
        return clockRepository.getLiveData();
    }

    //在数据库中获取所有时钟数据
    public LiveData<List<Clock>> getAllClocksLiveDataInDB() {
        return clockRepository.getAllClocksLiveDataInDB();
    }

    //在服务器中中获取所有时钟数据
    public void getAllClocksLiveDataInServer() {
        clockRepository.getAllClocksInServer();
    }

    //通过模糊查询出与关键子相似的时钟
    public LiveData<List<Clock>> getClocksLiveDataByPatternInDB(String pattern) {
        return clockRepository.getClocksLiveDataByPatternInDB(pattern);
    }

    //根据用户id从本地数据库清空该用户的时钟列表
    public void deleteClocksByUidInDB(List<Clock> clocks) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (clocks == null || clocks.size() == 0) {
            return;
        }
        Clock[] clockArray = new Clock[clocks.size()];
        clocks.toArray(clockArray);
        clockRepository.deleteClocksByUidInDB(clockArray);
    }

    //清空数据库中的时钟数据，并对添加新数据
    public void deleteAllClocksAndAdd(List<Clock> clocks) {
        Log.d("imageView", "deleteAllTaskAndAddInDB:");
        if (clocks == null || clocks.size() == 0) {
            return;
        }
        Clock[] clockArray = new Clock[clocks.size()];
        clocks.toArray(clockArray);
        clockRepository.deleteAllClocksAndAdd(clockArray);
    }

    //在数据库中添加时钟
    public void addClocksInDB(Clock... clocks) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (clocks.length == 0) return;
        clockRepository.addClocksToDB(clocks);
    }

    //在服务器中添加时钟
    public LiveData<ReturnData<Clock>> addClockToServer(Clock clock) {
        //在服务器中删除
        if (clock == null) {
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        return clockRepository.addClockToServer(clock);
    }

    //在数据库中更新时钟
    public void upDateClocksInDB(Clock... clocks) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (clocks.length == 0) return;
        clockRepository.updateClocksInDB(clocks);
    }

    //在服务器中中更新时钟
    public LiveData<ReturnData<Object>> upDateClocksInServer(Clock clock) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (clock == null) {
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        return clockRepository.updateClocksInServer(clock);
    }

    //在服务器中删除时钟
    public LiveData<ReturnData<Object>> deleteClocksByClockIdsInServer(List<Clock> clocks){
        Log.d("imageView", "deleteTasksByUidInServer: 删除任务");
        if (clocks == null || clocks.size() == 0) {
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        long[] clockIds = new long[clocks.size()];
        for (int i =0 ; i < clocks.size() ; i++) {
            clockIds[i] = clocks.get(i).getId();
        }
        return clockRepository.deleteClockByClockIdInServer(Arrays.toString(clockIds));
    }

    //根据用户id从本地数据库清空并添加任务列表
    public void deleteAllClocksAndAddInDB(List<Clock> clocks) {
        Log.d("imageView", "deleteAllClockAndAddInDB: ");
        if (clocks == null || clocks.size() ==0) {
            return;
        }
        Clock[] arrayClocks = new Clock[clocks.size()];
        clocks.toArray(arrayClocks);
        clockRepository.deleteAllClocksAndAdd(arrayClocks);
    }

}