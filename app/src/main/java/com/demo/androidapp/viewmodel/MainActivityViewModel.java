package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;
import com.demo.androidapp.repository.AuthRepository;

public class MainActivityViewModel extends AndroidViewModel {

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }
}