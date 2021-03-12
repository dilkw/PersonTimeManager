package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.repository.AuthRepository;


public class UserInfoViewModel extends AndroidViewModel {

    private AuthRepository authRepository;

    private LiveData<ReturnData<User>> userReturnLiveData;

    public UserInfoViewModel(@NonNull Application application) {
        super(application);
        Log.d("imageView", "UserInfoViewModel:-=-=-=-=-= " + application.getClass().getName());
        authRepository = new AuthRepository(application);
        userReturnLiveData = authRepository.getUserInfoLiveData();
    }

    //获取AuthRepository中的returnLiveData
    public LiveData<ReturnData<User>> getUserInfoLiveData() {
        return this.userReturnLiveData;
    }

    //在服务器中中更新用户信息
    public LiveData<ReturnData<Object>> upDateUserInfoInServer(User user) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (user == null) {
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        return authRepository.upDateUserInfoLiveData(user);
    }

    //在服务器中删除用户信息注销用户
    public LiveData<ReturnData<Object>> deleteUserInfoInServer(User user) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (user == null) {
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        return authRepository.upDateUserInfoLiveData(user);
    }

}