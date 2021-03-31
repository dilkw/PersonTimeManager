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

import java.util.Objects;

public class ActiveViewModel extends AndroidViewModel {

    public MutableLiveData<String> codesLiveData;

    public AuthRepository authRepository;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ActiveViewModel(@NonNull Application application) {
        super(application);
        this.codesLiveData = new MutableLiveData<String>("");
        authRepository = new AuthRepository(application);
    }

    public void setCodesLiveData(MutableLiveData<String> codesLiveData) {
        this.codesLiveData = codesLiveData;
    }

    public MutableLiveData<String> getCodesLiveData() {
        return codesLiveData;
    }

    public MutableLiveData<ReturnData<LoginAndRegisterReturn>> getReturnLiveData() {
        return authRepository.getReturnDataLiveData();
    }

    /**
     * email:注册页面填写的邮箱
     * codes:接收到的帐号激活码
     */
    public LiveData<ReturnData<Object>> active(String code) {
        Log.d("imageView", "active: 邮箱" + email + "验证码" + code);
        if (email == null || email.equals("") || code.equals("")) {
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        return authRepository.active(email,code);
    }

    /**
     * 重新获取验证码
     * email:帐号关联的邮箱
     */
    public LiveData<ReturnData<Object>> getActiveCodes() {
        if (email == null && email.equals("")) {
           return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        return authRepository.getActiveCode(email);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("imageView", "ActiveViewModel------onCleared: ");
    }
}