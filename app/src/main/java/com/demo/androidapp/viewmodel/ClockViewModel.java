package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.demo.androidapp.model.entity.Bill;
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
    public LiveData<List<Clock>> getReturnLiveData() {
        return clockRepository.getLiveData();
    }

    //获取clockRepository中的returnLiveData
    public LiveData<List<Clock>> getClocksLiveDataByPattern(String s) {
        return clockRepository.getClocksLiveDataByPattern(s);
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

    //更新时钟
    public void upDateClocksInDB(Clock... clocks) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (clocks.length == 0) return;
        clockRepository.updateTasks(clocks);
    }

}