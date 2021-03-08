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

    //获取clockRepository中的returnLiveData
    public LiveData<List<Clock>> getClocksLiveDataByPattern(String s) {
        return clockRepository.getClocksLiveDataByPatternInDB(s);
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

    //添加时钟
    public void addClocksInDB(Clock... clocks) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (clocks.length == 0) return;
        clockRepository.addClocksToDB(clocks);
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

    public LiveData<ReturnData<Object>> deleteClockByClockIdInServer(Clock clock){
        return clockRepository.deleteClockByClockIdInServer(clock.getId());
    }

}