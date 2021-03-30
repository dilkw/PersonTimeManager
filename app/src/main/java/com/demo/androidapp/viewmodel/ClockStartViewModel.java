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

public class ClockStartViewModel extends AndroidViewModel {

    public MutableLiveData<Clock> clockLiveData;

    private ClockRepository clockRepository;

    public ClockStartViewModel(@NonNull Application application) {
        super(application);
        clockRepository = new ClockRepository(application);
        clockLiveData = new MutableLiveData<>();
        Log.d("imageView", "HomeViewModel:-=-=-=-=-= " + application.getClass().getName());
    }

    //根据时钟id获取时钟信息（网络数据库）
    public LiveData<ReturnData<Clock>> getClockInfoInServer(long clockId) {
        return clockRepository.getClockInfoInServer(clockId);
    }

    //根据时钟id获取时钟信息（网络数据库）
    public LiveData<Clock> getClockInfoInDB(long clockId) {
        return clockRepository.getClockInfoInDB(clockId);
    }

    //更新时钟信息（网络数据库）
    public LiveData<ReturnData<Object>> upDateClockInfoInServer(Clock clock) {
        return clockRepository.updateClocksInServer(clock);
    }

}